package yinlei.com.httpstudy;

import android.util.Log;
import android.view.View;

import java.util.Enumeration;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DownloadTask implements Runnable{
    private final DownloadEntry mEntry;

    public DownloadTask(DownloadEntry entry) {
        mEntry = entry;
    }

    public boolean isPaused = false;
    public boolean isCancel = false;

    public void pause() {
        isPaused = true;

    }

    public void cancel() {
        isCancel = true;
    }

    public void start() {
        mEntry.mStatus = DownloadEntry.DownloadStatus.downloading;
        DataChanger.getmInstance().postStatus(mEntry);

        mEntry.totalLength = 1024 * 100;

        for (int i = mEntry.curentLength; i < mEntry.totalLength; i++) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isPaused || isCancel) {
                mEntry.mStatus = isPaused ?
                        DownloadEntry.DownloadStatus.pause:
                        DownloadEntry.DownloadStatus.cancel;
                DataChanger.getmInstance().postStatus(mEntry);
                // TODO: 2016/11/22  if canceled  delete related file
                return;
            }
            i += 1024;
            mEntry.curentLength += 1024;
            DataChanger.getmInstance().postStatus(mEntry);
            Log.d("DownloadTask", "i:" + i);
        }

        mEntry.mStatus = DownloadEntry.DownloadStatus.complete;

    }

    @Override
    public void run() {
        start();
    }
    // TODO: 2016/11/22  check if support range,
}
