package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordFileFragment extends BaseFragment {
    @BindView(R.id.rcvList)
    RecyclerView recyclerView;

    public static RecordFileFragment newInstance() {
        Bundle args = new Bundle();
        RecordFileFragment fragment = new RecordFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.record_file_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void initData() {
        super.initData();

    }

    private void initFileRecord() {

    }

    @OnClick(R.id.tvAdd)
    public void onAddClick() {

    }
}
