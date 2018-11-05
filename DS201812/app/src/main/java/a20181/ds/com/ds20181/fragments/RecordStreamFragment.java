package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.view.View;


import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileRecord;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordStreamFragment extends BaseFragment implements AppConstant {
    public static RecordStreamFragment newInstance(String id) {
        Bundle args = new Bundle();
        RecordStreamFragment fragment = new RecordStreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.record_stream_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.addRecord)
    public void addStreamContent() {
        FileRecord record = new FileRecord();
        record.setUserId("Thân Tài Linh");
        record.setContent("Hello every one");
        record.setTime(70);
        bus.post(AppAction.ADD_RECORD.setData(record));
    }

}
