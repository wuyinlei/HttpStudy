package yinlei.com.httpstudy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {


    private DownloadManager mDownloadManager;
    private List<DownloadEntry> mDownloadEntries = new ArrayList<>();
    private DataWatcher mWatcher = new DataWatcher() {
        @Override
        public void notifyUpdate(DownloadEntry data) {
            int index = mDownloadEntries.indexOf(data);
            if (index !=-1) {
                mDownloadEntries.remove(index);
                mDownloadEntries.add(index,data);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private ListView mListView;
    private DownloadAdapter mAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mDownloadManager = DownloadManager.getInstance(this);

        mDownloadEntries.add(new DownloadEntry("10","ruolan1","http://api.test.com/uploads/test0",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("11","ruolan2","http://api.test.com/uploads/test1",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("12","ruolan3","http://api.test.com/uploads/test2",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("13","ruolan4","http://api.test.com/uploads/test3",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("14","ruolan5","http://api.test.com/uploads/test4",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("15","ruolan6","http://api.test.com/uploads/test5",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("16","ruolan7","http://api.test.com/uploads/test6",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("17","ruolan8","http://api.test.com/uploads/test7",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("18","ruolan9","http://api.test.com/uploads/test8",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("19","ruolan10","http://api.test.com/uploads/test9",DownloadEntry.DownloadStatus.idle));
        mDownloadEntries.add(new DownloadEntry("20","ruolan11","http://api.test.com/uploads/test10",DownloadEntry.DownloadStatus.idle));

        mListView = (ListView) findViewById(R.id.list_item);
        mAdapter = new DownloadAdapter();
        mListView.setAdapter(mAdapter);
    }

    class DownloadAdapter extends BaseAdapter{

        private ViewHolder mViewHolder;

        @Override
        public int getCount() {
            return mDownloadEntries==null ? 0 : mDownloadEntries.size();
        }

        @Override
        public Object getItem(int i) {
            return mDownloadEntries.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null|| view.getTag() == null){
                view = LayoutInflater.from(ListActivity.this)
                        .inflate(R.layout.activity_list_item,null);
                mViewHolder = new ViewHolder();
                mViewHolder.mDownloadBtn = (Button) view.findViewById(R.id.btn_start);
                mViewHolder.mDownloadLabel = (TextView) view.findViewById(R.id.btn_label);
                view.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) view.getTag();
            }
            final DownloadEntry entry = mDownloadEntries.get(i);
            mViewHolder.mDownloadLabel.setText(entry.name+" is " + entry.mStatus+ "  "
            + entry.id + entry.curentLength+ " / "  +entry.totalLength);
            mViewHolder.mDownloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (entry.mStatus == DownloadEntry.DownloadStatus.idle){
                      //  entry.mStatus = DownloadEntry.DownloadStatus.downloading;
                        mDownloadManager.add(ListActivity.this,entry);
                    } else if (entry.mStatus == DownloadEntry.DownloadStatus.downloading
                    || entry.mStatus == DownloadEntry.DownloadStatus.waiting){
                        mDownloadManager.pause(entry);
                    } else if (entry.mStatus == DownloadEntry.DownloadStatus.paused){
                        mDownloadManager.resume(entry);
                    }
                }
            });

            return view;
        }

    }

    class ViewHolder {
        private Button mDownloadBtn;
        TextView mDownloadLabel;
    }
}
