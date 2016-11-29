package yinlei.com.httpstudy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DataChanger extends Observable {

    private static DataChanger mInstance;

    private LinkedHashMap<String, DownloadEntry> mOperatedEntries;

    private DataChanger() {
        mOperatedEntries = new LinkedHashMap<>();
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
        mOperatedEntries.put(entry.id, entry);
        setChanged();
        notifyObservers(entry);
    }

    /**
     * 查询所有状态处于暂停的任务
     *
     * @return 需要恢复的任务集合
     */
    public List<DownloadEntry> queryAllRecoverableEntries() {
        List<DownloadEntry> mRecoverableEntries = null;

        for (Map.Entry<String,DownloadEntry> entry:mOperatedEntries.entrySet()) {
            if (entry.getValue().mStatus == DownloadEntry.DownloadStatus.paused) {
                if (mRecoverableEntries == null) {
                    mRecoverableEntries = new ArrayList<>();
                }
                mRecoverableEntries.add(entry.getValue());
            }
        }
        return mRecoverableEntries;
    }
}
