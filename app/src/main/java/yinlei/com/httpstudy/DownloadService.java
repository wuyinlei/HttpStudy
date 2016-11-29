package yinlei.com.httpstudy;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class DownloadService extends Service {

    private HashMap<String, DownloadTask> mDownloadTaskHashMap = new HashMap<>();
    private ExecutorService mExecutorService;

    public static final int NOTIFY_DOWNLOADING = 1;
    public static final int NOTIFY_UPDATING = 2;
    public static final int NOTIFY_PAUSED_OR_CANCELLED = 3;
    public static final int NOTIFY_COMPLETED = 4;

    private List<DownloadEntry> mPausedEntries = new ArrayList<>();

    //任务队列  这里手动的去维护三个线程,来提高效率,控制最大下载数
    private LinkedBlockingDeque<DownloadEntry> mWaitingQueue = new LinkedBlockingDeque<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //DownloadEntry entry = (DownloadEntry) msg.obj;
            switch (msg.what) {
                case NOTIFY_PAUSED_OR_CANCELLED:
                   // mPausedEntries.add((DownloadEntry) msg.obj);
                case NOTIFY_COMPLETED:
                    checkNext();
                    break;
            }
            DataChanger.getmInstance().postStatus((DownloadEntry) msg.obj);
        }
    };

    /**
     * 检查下一个任务
     */
    private void checkNext() {
        // 获取并移除此双端队列表示的队列的头部（即此双端队列的第一个元素）；
        // 如果此双端队列为空，则返回 null。
        DownloadEntry newEntry = mWaitingQueue.poll();
        if (newEntry != null) {
            startDownload(newEntry);
        }
    }

    public DownloadService() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutorService = Executors.newCachedThreadPool();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownloadEntry entry =
                (DownloadEntry) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ACTION, -1);
        doAction(action, entry);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action, DownloadEntry entry) {
        //check action  do  related action
//        if (action == Constants.KEY_DOWNLOAD_ACTION_ADD) {
//            entry.mStatus = DownloadEntry.DownloadStatus.downloading;
//            DataChanger.getmInstance().postStatus(entry);
//        }

        switch (action) {
            case Constants.KEY_DOWNLOAD_ACTION_ADD:
                addDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE:
                pauseDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RESUME:
                resumeDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_CANCEL:
                cancelDownload(entry);
                break;
            case Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL:
                pauseAll();
                break;
            case Constants.KEY_DOWNLOAD_ACTION_RECOVER_ALL:
                recoverAll();
                break;

        }

    }

    /**
     * 恢复所有
     */
    private void recoverAll() {

        List<DownloadEntry> allRecoverableEntries = DataChanger.getmInstance().queryAllRecoverableEntries();
        if (allRecoverableEntries != null){
            for (DownloadEntry entry : allRecoverableEntries) {
                addDownload(entry);
            }
        }

    }

    /**
     * 暂停所有
     */
    private void pauseAll() {

        while (mWaitingQueue.iterator().hasNext()){
            DownloadEntry entry = mWaitingQueue.poll();
            entry.mStatus = DownloadEntry.DownloadStatus.paused;

            DataChanger.getmInstance().postStatus(entry);
        }

        for (Map.Entry<String,DownloadTask> entry : mDownloadTaskHashMap.entrySet()){
            entry.getValue().pause();
        }

        mDownloadTaskHashMap.clear();
    }

    /**
     * 添加到请求队列
     *
     * @param entry DownloadEntry
     */
    private void addDownload(DownloadEntry entry) {
        if (mDownloadTaskHashMap.size() >= Constants.MAX_DOWNLOAD_TASKS) {
            // 如果立即可行且不违反容量限制，则将指定的元素插入此双端队列表示的队列中（即此双端队列的尾部）
            // 并在成功时返回 true；如果当前没有空间可用，则返回 false。
            mWaitingQueue.offer(entry);
            //添加到队列中,状态改为waiting
            entry.mStatus = DownloadEntry.DownloadStatus.waiting;
            DataChanger.getmInstance().postStatus(entry); //通知更新
        } else {
            startDownload(entry);
        }
    }

    /**
     * 取消任务
     *
     * @param entry DownloadEntry
     */
    private void cancelDownload(DownloadEntry entry) {
        DownloadTask task = mDownloadTaskHashMap.remove(entry.id);
        if (task != null) {
            task.cancel();
        } else {
            mWaitingQueue.remove(entry);
            entry.mStatus = DownloadEntry.DownloadStatus.cancel;
            DataChanger.getmInstance().postStatus(entry);
        }
    }

    /**
     * 恢复下载任务
     *
     * @param entry DownloadEntry
     */
    private void resumeDownload(DownloadEntry entry) {
        addDownload(entry);
    }

    /**
     * 取消任务
     *
     * @param entry DownloadEntry
     */
    private void pauseDownload(DownloadEntry entry) {
        DownloadTask task = mDownloadTaskHashMap.remove(entry.id);
        if (task != null) {
            task.pause();
        } else {
            mWaitingQueue.remove(entry);
           // mPausedEntries.add(entry);
            entry.mStatus = DownloadEntry.DownloadStatus.paused;
            DataChanger.getmInstance().postStatus(entry);
        }
    }

    /**
     * 开始下载任务
     *
     * @param entry DownloadEntry
     */
    private void startDownload(DownloadEntry entry) {
        DownloadTask downloadTask = new DownloadTask(entry, mHandler);
        mDownloadTaskHashMap.put(entry.id, downloadTask);
        //downloadTask.start();
        mExecutorService.execute(downloadTask);
    }
}
