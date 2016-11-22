package yinlei.com.httpstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DownloadEntry entry =
                (DownloadEntry) intent.getSerializableExtra(Constants.KEY_DOWNLOAD_ENTRY);
        int action = intent.getIntExtra(Constants.KEY_DOWNLOAD_ENTRY,-1);
        doAction(action,entry);
        return super.onStartCommand(intent, flags, startId);
    }

    private void doAction(int action,DownloadEntry entry) {
        //check action  do  related action
        if (action == Constants.KEY_DOWNLOAD_ACTION_ADD){
            entry.mStatus = DownloadEntry.DownloadStatus.downloading;
            DataChanger.getmInstance().postStatus(entry);
        }

    }
}
