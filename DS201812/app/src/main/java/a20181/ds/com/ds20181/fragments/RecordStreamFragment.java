package a20181.ds.com.ds20181.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.customs.InputFilterMinMax;
import a20181.ds.com.ds20181.models.CreateRecordBody;
import a20181.ds.com.ds20181.models.CreateRecordBody.*;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.models.ResponseTemporary;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecordStreamFragment extends BaseFragment implements AppConstant {
    private String idFile;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    int x = 10;

    public static RecordStreamFragment newInstance(String id) {
        Bundle args = new Bundle();
        RecordStreamFragment fragment = new RecordStreamFragment();
        fragment.idFile = id;
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
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (getActivity() != null) getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {
        super.initData();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragmentAB, StreamABFragment.newInstance(idFile), StreamABFragment.class.getSimpleName())
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.fragmentBC, StreamBCFragment.newInstance(idFile), StreamABFragment.class.getSimpleName())
                .commit();
        getAllTemporary();
    }

    private void getAllTemporary() {
        if (app.getCurrentUser() == null || StringUtils.isEmpty(idFile)) return;
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showLoading(true);
        }
        Disposable disposable = AppClient.getAPIService().getAllTemporary(app.getCookie(), idFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseTemporary>() {
                    @Override
                    public void accept(ResponseTemporary responseTemporary) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        if (responseTemporary != null) {
                            List<DataAB> dataABS = responseTemporary.getDataABS();
                            List<DataBC> dataBCS = responseTemporary.getDataBCS();
                            bus.post(AppAction.RESPONSE_DATA_WHO.setData(dataABS));
                            bus.post(AppAction.RESPONSE_DATA_WHAT.setData(dataBCS));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void createRecord(CreateRecordBody createRecordBody) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showLoading(true);
        }
        Disposable disposable = AppClient.getAPIService().importRecord(app.getCookie(), idFile, createRecordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FileRecord>>() {
                    @Override
                    public void accept(List<FileRecord> list) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        Log.e("accept: ", list.size() + " ");
                        bus.post(AppAction.ADD_RECORD);
                        showDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);

    }

    public void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setMessage("Nhập file thành công");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @OnClick(R.id.finishStream)
    public void finishStreamClick() {
        FragmentManager f = getChildFragmentManager();
        StreamABFragment fragmentAB = (StreamABFragment) f.findFragmentById(R.id.fragmentAB);
        StreamBCFragment fragmentBC = (StreamBCFragment) f.findFragmentById(R.id.fragmentBC);
        List<DataAB> recordAB = fragmentAB.getRecordAB();
        List<DataBC> recordBC = fragmentBC.getRecordBC();
        if (recordAB.isEmpty() && recordBC.isEmpty()) {
            Toast.makeText(getContext(), "Bạn chưa nhập các file cần thiết", Toast.LENGTH_SHORT).show();
        } else {
            CreateRecordBody createRecordBody = new CreateRecordBody();
            List<CreateRecordBody.DataAB> data1 = new ArrayList<>();
            List<CreateRecordBody.DataBC> data2 = new ArrayList<>();
            createRecordBody.setData1(recordAB);
            createRecordBody.setData2(recordBC);
            createRecordBody.setTime(System.currentTimeMillis());
            createRecord(createRecordBody);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
