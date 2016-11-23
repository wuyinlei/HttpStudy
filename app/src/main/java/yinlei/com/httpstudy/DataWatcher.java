package yinlei.com.httpstudy;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public abstract class DataWatcher implements Observer {
    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof DownloadEntry) {
            notifyUpdate((DownloadEntry) data);
        }
    }

    public abstract void notifyUpdate(DownloadEntry data);

}

