package yinlei.com.httpstudy;

import android.os.Handler;
import android.os.Message;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DownloadTask implements Runnable{
    private final DownloadEntry mEntry;

    public DownloadTask(DownloadEntry entry) {
        mEntry = entry;
    }

    public volatile boolean isPaused = false;
    public volatile boolean isCancel = false;

    private Handler mHandler;

    /**
     * 构造器
     * @param entry DownloadEntry
     * @param handler  用于把结果分发到主线程中更新UI
     */
    public DownloadTask(DownloadEntry entry, Handler handler) {
        this.mEntry = entry;
        this.mHandler = handler;
    }


    public void pause() {
        isPaused = true;
    }

    public void cancel() {
        isCancel = true;
    }

    public void start() {
        mEntry.mStatus = DownloadEntry.DownloadStatus.downloading;
        notifyUpdate(mEntry,DownloadService.NOTIFY_DOWNLOADING);
        Message message;
        //DataChanger.getmInstance().postStatus(mEntry);  这个是在子线程会带哦的

        mEntry.totalLength = 1024 * 100;

        for (int i = mEntry.curentLength; i < mEntry.totalLength; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isPaused || isCancel) {
                mEntry.mStatus = isPaused ?
                        DownloadEntry.DownloadStatus.paused :
                        DownloadEntry.DownloadStatus.cancel;
//                message = mHandler.obtainMessage();
//                message.obj = mEntry;
//                mHandler.sendMessage(message);
                notifyUpdate(mEntry,DownloadService.NOTIFY_PAUSED_OR_CANCELLED);
                //DataChanger.getmInstance().postStatus(mEntry);
                // TODO: 2016/11/22  if canceled  delete related file
                return;
            }
            i += 1024;
            mEntry.curentLength += 1024;
//            message = mHandler.obtainMessage();
//            message.obj = mEntry;
//            mHandler.sendMessage(message);
//           // DataChanger.getmInstance().postStatus(mEntry);
//            Log.d("DownloadTask", "i:" + i);
            notifyUpdate(mEntry,DownloadService.NOTIFY_UPDATING);
        }

        mEntry.mStatus = DownloadEntry.DownloadStatus.complete;
        //DataChanger.getmInstance().postStatus(mEntry);
        notifyUpdate(mEntry,DownloadService.NOTIFY_COMPLETED);

    }

    private void notifyUpdate(DownloadEntry entry,int what) {
        Message message = Message.obtain();
        message.what = what;
        message.obj = entry;
        //线程中转
        mHandler.sendMessage(message);  //在这里做成回调到主线程  直接在回调的时候可以更新UI
    }

    @Override
    public void run() {
        start();
    }
    // TODO: 2016/11/22  check if support range,
}
