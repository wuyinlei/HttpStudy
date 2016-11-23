package yinlei.com.httpstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadService extends Service {

    private HashMap<String, DownloadTask> mDownloadTaskHashMap = new HashMap<>();
    private ExecutorService mExecutorService;

    public DownloadService() {
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
                startDownload(entry);
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

        }

    }

    private void cancelDownload(DownloadEntry entry) {
        DownloadTask task = mDownloadTaskHashMap.remove(entry.id);
        if (task != null) {
            task.cancel();
        }
    }

    private void resumeDownload(DownloadEntry entry) {
startDownload(entry);
    }

    private void pauseDownload(DownloadEntry entry) {
        DownloadTask task = mDownloadTaskHashMap.remove(entry.id);
        if (task != null) {
            task.pause();
        }
    }

    private void startDownload(DownloadEntry entry) {
        DownloadTask downloadTask = new DownloadTask(entry);
        mDownloadTaskHashMap.put(entry.id, downloadTask);
        //downloadTask.start();
        mExecutorService.execute(downloadTask);
    }
}
