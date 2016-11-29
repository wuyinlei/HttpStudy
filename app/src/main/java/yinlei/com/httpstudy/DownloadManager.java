package yinlei.com.httpstudy;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DownloadManager {

    private static DownloadManager sDownloadManager;

    private static  Context mContext;

    private static final int MIN_OPERATE_INTERVAL = 1000 * 1;
    private long mLastOperatedTime = 0;

    public DownloadManager(Context context) {
        mContext = context;
    }

    public static synchronized DownloadManager getInstance(Context context) {
        if (sDownloadManager == null) {
            sDownloadManager = new DownloadManager(context);
        }
        mContext = context;
        return sDownloadManager;
    }

    /**
     * 添加任务
     *
     * @param downloadEntry DownloadEntry
     */
    public void add(DownloadEntry downloadEntry) {

        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, downloadEntry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_ADD);
        mContext.startService(intent);
    }

    private boolean checkIfExecutable() {
        long temp = System.currentTimeMillis();
        if (temp - mLastOperatedTime > MIN_OPERATE_INTERVAL){
            mLastOperatedTime = temp;
            return true;
        }
        return false;
    }

    /**
     * 暂停任务
     *
     * @param entry DownloadEntry
     */
    public void pause(DownloadEntry entry) {
        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_PAUSE);
        mContext.startService(intent);
    }

    /**
     * 恢复下载任务
     *
     * @param entry DownloadEntry
     */
    public void resume(DownloadEntry entry) {
        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_RESUME);
        mContext.startService(intent);
    }

    /**
     * 取消下载任务
     *
     * @param entry DownloadEntry
     */
    public void cancel(DownloadEntry entry) {
        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY, entry);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION, Constants.KEY_DOWNLOAD_ACTION_CANCEL);
        mContext.startService(intent);
    }

    public void pauseAll(){
        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext,DownloadService.class);
       // intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_PAUSE_ALL);
        mContext.startService(intent);
    }

    public void recoverAll(){
        if (!checkIfExecutable())
            return;
        Intent intent = new Intent(mContext,DownloadService.class);
        // intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,);
        intent.putExtra(Constants.KEY_DOWNLOAD_ACTION,Constants.KEY_DOWNLOAD_ACTION_RECOVER_ALL);
        mContext.startService(intent);
    }

    /**
     * 添加观察者
     *
     * @param watcher 数据观察者
     */
    public void addObserver(DataWatcher watcher) {
        DataChanger.getmInstance().addObserver(watcher);
    }

    /**
     * 移除观察者
     *
     * @param watcher 数据观察者
     */
    public void removeObserver(DataWatcher watcher) {
        DataChanger.getmInstance().deleteObserver(watcher);
    }
}
