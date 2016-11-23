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


    public enum DownloadStatus{waiting,downloading,pause,resume,cancel,complete}

    public DownloadStatus mStatus;

}
