package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.FileFilmAdapter;
import a20181.ds.com.ds20181.adapters.RecordAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static a20181.ds.com.ds20181.AppConstant.EMPTY;
import static a20181.ds.com.ds20181.AppConstant.app;

public class RecordContentFragment extends BaseFragment implements RecordAdapter.ItemClickListener {

    @BindView(R.id.rcv_content_recorded)
    RecyclerView rcvContent;

    private RecordAdapter recordAdapter;

    private List<FileRecord> fileRecords;
    private String id = EMPTY;

    @Override
    public int getLayoutResource() {
        return R.layout.record_content_fragment;
    }

    public static RecordContentFragment newInstance(String id) {
        Bundle args = new Bundle();
        RecordContentFragment fragment = new RecordContentFragment();
        fragment.id = id;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        if (getContext() != null) {
            recordAdapter = new RecordAdapter(getContext(), this);
            rcvContent.setLayoutManager(new LinearLayoutManager(getContext()));
            rcvContent.setAdapter(recordAdapter);
        }

    }

    @Override
    public void initData() {
        super.initData();
        initFileRecord();

    }

    private void initFileRecord() {
        fileRecords = new ArrayList<>();

        User user = app.getCurrentUser();
        if (user == null) {
            return;
        }
        Log.e("initFileRecord: ", user.getUserId() + " xx " + app.getCurrentUser().getCookie());
        Observable<List<FileRecord>> request = AppClient.getAPIService().getRecordFile(app.getCurrentUser().getCookie(), id);

        request.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (getActivity() != null)
                            ((MainActivity) getActivity()).showLoading(false);
                        List<FileRecord> result = (ArrayList) o;
                        Log.d("data size", "accept: " + result.size());
                        recordAdapter.set(result);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
