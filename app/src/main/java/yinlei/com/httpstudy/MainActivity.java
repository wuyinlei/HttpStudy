package yinlei.com.httpstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnStrat, mBtnPause, mBtnCancel;

    private DownloadManager mDownloadManager;

    private DataWatcher mWatcher = new DataWatcher() {
        @Override
        public void notifyUpdate(DownloadEntry data) {
            // Log.d("MainActivity", "data:" + data.curentLength);
            if (data.mStatus == DownloadEntry.DownloadStatus.pause) {
                mEntry = data;
            } else if (data.mStatus == DownloadEntry.DownloadStatus.cancel) {
                mEntry = null;
            }
        }
    };
    private DownloadEntry mEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDownloadManager = new DownloadManager(this);
        mBtnStrat = (Button) findViewById(R.id.btn_start);
        mBtnStrat.setOnClickListener(this);
        mBtnPause = (Button) findViewById(R.id.btn_pause);
        mBtnPause.setOnClickListener(this);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mDownloadManager.addObserver(mWatcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDownloadManager.removeObserver(mWatcher);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (mEntry == null) {
                    mEntry = new DownloadEntry();
                    mEntry.name = "test.jpg";
                    mEntry.url = "http://api.wuyinlei.com/uploads/test.jpg";
                    mEntry.id = "1";
                    mEntry.mStatus = DownloadEntry.DownloadStatus.downloading;
                    mDownloadManager.add(this, mEntry);
                }

                break;

            case R.id.btn_pause:
                if (mEntry.mStatus == DownloadEntry.DownloadStatus.pause) {
                    mDownloadManager.resume(mEntry);
                } else if (mEntry.mStatus == DownloadEntry.DownloadStatus.downloading) {
                    mDownloadManager.pause(mEntry);
                }
                break;

            case R.id.btn_cancel:
                mDownloadManager.cancel(mEntry);
                break;
        }
    }
}
