package yinlei.com.httpstudy;

import java.io.Serializable;

/**
 * Created by wuyinlei on 2016/11/22.
 */

public class DownloadEntry implements Serializable {

    public String id;
    public String name;
    public String url;


    public int progress;
    public int totalLength;
    public int curentLength;

    public DownloadEntry() {
    }

    public DownloadEntry(String id, String name, String url,DownloadStatus status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mStatus = status;
    }

    public enum DownloadStatus{idle,waiting,downloading, paused, resumed,cancel,complete}

    public DownloadStatus mStatus;


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj !=null)
            return obj.hashCode() == this.hashCode();
        return super.equals(obj);
    }
}
