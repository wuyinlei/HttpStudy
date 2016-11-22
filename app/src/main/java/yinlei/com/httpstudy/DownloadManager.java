package yinlei.com.httpstudy;

import android.content.Context;
import android.content.Intent;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DownloadManager {

    private static DownloadManager sDownloadManager;

    private final Context mContext;

    public DownloadManager(Context context) {
        mContext = context;
    }

    public synchronized DownloadManager getInstance(Context context){
        if (sDownloadManager == null){
            sDownloadManager = new DownloadManager(context);
        }
        return sDownloadManager;
    }

    public void add(Context context, DownloadEntry downloadEntry){
        Intent intent = new Intent(context,DownloadService.class);
        intent.putExtra(Constants.KEY_DOWNLOAD_ENTRY,downloadEntry);
        context.startService(intent);
    }

    public void pause(){}

    public void resume(){}

    public void cancel(){}

    public void addObserver(DataWatcher watcher){
        DataChanger.getmInstance().addObserver(watcher);
    }

    public void removeObserver(DataWatcher watcher){
        DataChanger.getmInstance().deleteObserver(watcher);
    }
}
