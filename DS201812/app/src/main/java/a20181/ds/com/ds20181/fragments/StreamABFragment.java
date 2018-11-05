package a20181.ds.com.ds20181.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.BaseRecyclerViewAdapter;
import a20181.ds.com.ds20181.adapters.ContentStreamABAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileRecord;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamABFragment extends BaseFragment implements BaseRecyclerViewAdapter.ItemClickListener {
    @BindView(R.id.rcvList)
    RecyclerView rcvList;
    ContentStreamABAdapter adapter;

    @Override
    public int getLayoutResource() {
        return R.layout.stream_xx_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContentStreamABAdapter(getContext(), this);
        rcvList.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Subscribe
    public void onAppAction(AppAction appAction) {
        if(appAction == AppAction.ADD_RECORD) {
            FileRecord record = appAction.getData(FileRecord.class);
            adapter.addItem(record);
        }
    }
}
