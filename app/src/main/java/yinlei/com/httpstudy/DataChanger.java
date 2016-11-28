package yinlei.com.httpstudy;

import java.util.Observable;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DataChanger extends Observable {

    private static DataChanger mInstance;

    private DataChanger() {

    }

    /**
     * 获取到对象
     *
     * @return
     */
    public synchronized static DataChanger getmInstance() {
        if (mInstance == null) {
            mInstance = new DataChanger();
        }
        return mInstance;
    }

    public void postStatus(DownloadEntry entry) {
        setChanged();
        notifyObservers(entry);
    }
}
