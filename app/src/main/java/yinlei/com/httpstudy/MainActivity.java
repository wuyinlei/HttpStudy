package yinlei.com.httpstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnStrat, mBtnPause, mBtnCancel;

    private DownloadManager mDownloadManager;

    private DataWatcher mWatcher = new DataWatcher() {
        @Override
        public void notifyUpdate(DownloadEntry data) {
            // Log.d("MainActivity", "data:" + data.curentLength);
            if (data.mStatus == DownloadEntry.DownloadStatus.paused) {
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
                if (mEntry == null) {  //如果DownloadEntry为空,那么就重新赋值,也就是重新创建一个下载任务
                    mEntry = new DownloadEntry();
                    mEntry.name = "test.jpg";
                    mEntry.url = "http://api.wuyinlei.com/uploads/test.jpg";
                    mEntry.id = "1";
                    mEntry.mStatus = DownloadEntry.DownloadStatus.downloading;
                    mDownloadManager.add( mEntry);  //添加到下载
                }

                break;

            case R.id.btn_pause:
                if (mEntry.mStatus == DownloadEntry.DownloadStatus.paused) {  //如果当前的状态是pause,那么在此点击的时候会去恢复状态
                    mDownloadManager.resume(mEntry);
                } else if (mEntry.mStatus == DownloadEntry.DownloadStatus.downloading) { //如果是下载中,那么在此点击就是暂停
                    mDownloadManager.pause(mEntry);
                }
                break;

            case R.id.btn_cancel:
                mDownloadManager.cancel(mEntry);  //取消
                break;
        }
    }
}
