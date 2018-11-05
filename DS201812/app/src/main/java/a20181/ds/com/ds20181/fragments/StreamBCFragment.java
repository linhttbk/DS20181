package a20181.ds.com.ds20181.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.ContentStreamBCAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileRecord;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StreamBCFragment extends BaseFragment  {
    @BindView(R.id.rcvList)
    RecyclerView rcvList;
    ContentStreamBCAdapter adapter;

    @Override
    public int getLayoutResource() {
        return R.layout.stream_xx_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        rcvList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContentStreamBCAdapter(getContext(), null);
        rcvList.setAdapter(adapter);
    }

    @Override
    public void initData() {
        super.initData();
    }


    @Subscribe
    public void onAppAction(AppAction appAction) {
        if(appAction == AppAction.ADD_RECORD) {
            FileRecord record = appAction.getData(FileRecord.class);
            adapter.addItem(record);
        }
    }

}
