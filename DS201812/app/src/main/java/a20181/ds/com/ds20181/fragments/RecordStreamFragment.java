package a20181.ds.com.ds20181.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import a20181.ds.com.ds20181.models.FileRecord;
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
       view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(getActivity()!=null) getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        } );
    }

    @Override
    public void initData() {
        super.initData();

    }
    //    @OnClick(R.id.addRecord)
//    public void addStreamContent() {
//        showDialog();
//
//
//    }

//    private void showDialog() {
//        final Dialog dialog = new Dialog(getContext());
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.setContentView(R.layout.add_record_dialog);
//        final EditText edtName = dialog.findViewById(R.id.edtName);
//        final EditText edtContent = dialog.findViewById(R.id.edtContent);
//        final EditText edtHour = dialog.findViewById(R.id.edtHour);
//        final EditText edtMinutes = dialog.findViewById(R.id.edtMinutes);
//        final EditText edtSecond = dialog.findViewById(R.id.edtSecond);
//        Button create = dialog.findViewById(R.id.btn_create);
//        Button cancel = dialog.findViewById(R.id.btn_cancel);
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //  dialog.dismiss();
//                if (app.getCurrentUser() == null) return;
//                else {
//                    String name = edtName.getText().toString().trim();
//                    String content = edtContent.getText().toString().trim();
//                    String hourString = edtHour.getText().toString().trim();
//                    String minString = edtMinutes.getText().toString().trim();
//                    String secString = edtSecond.getText().toString().trim();
//
//                    if (StringUtils.isEmpty(name) || StringUtils.isEmpty(content) || StringUtils.isEmpty(hourString) || StringUtils.isEmpty(minString) || StringUtils.isEmpty(secString)) {
//                        Toast.makeText(getContext(), getString(R.string.msg_fill_infor), Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        int hour = Integer.parseInt(hourString);
//                        int min = Integer.parseInt(minString);
//                        int sec = Integer.parseInt(secString);
//                        final FileRecord record = new FileRecord();
//                        record.setSpeaker(name);
//                        record.setContent(content);
//                        record.setTime(StringUtils.convertTime(hour, min, sec));
//                        CreateRecordBody createRecordBody = new CreateRecordBody();
//                        CreateRecordBody.DataAB dataAB = new CreateRecordBody.DataAB();
//                        CreateRecordBody.DataBC dataBC = new CreateRecordBody.DataBC();
//                        dataAB.setSpeaker(record.getSpeaker());
//                        dataAB.setTime(record.getTime());
//                        dataBC.setContent(record.getContent());
//                        dataBC.setTime(record.getTime());
//                        createRecordBody.setData1(dataAB);
//                        createRecordBody.setData2(dataBC);
//                        dialog.dismiss();
//                        createRecord(createRecordBody, record);
//                    }
//                }
//
//            }
//        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        edtMinutes.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
//        edtSecond.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
//        dialog.show();
//    }

    private void createRecord(CreateRecordBody createRecordBody) {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showLoading(true);
        }
        Disposable disposable = AppClient.getAPIService().addRecord(app.getCookie(), idFile, createRecordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileRecord>() {
                    @Override
                    public void accept(FileRecord fileRecord) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                        Log.e("accept: ", fileRecord.getId());
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
        List<FileRecord> recordAB = fragmentAB.getRecordAB();
        List<FileRecord> recordBC = fragmentBC.getRecordBC();
        if (recordAB.isEmpty() && recordBC.isEmpty()) {
            Toast.makeText(getContext(), "Bạn chưa nhập các file cần thiết", Toast.LENGTH_SHORT).show();
        } else {
            int countAB = recordAB.size();
            int countBC = recordBC.size();
            if (countAB == countBC) {
                CreateRecordBody createRecordBody = new CreateRecordBody();
                List<CreateRecordBody.DataAB> data1 = new ArrayList<>();
                List<CreateRecordBody.DataBC> data2 = new ArrayList<>();
                for (FileRecord fileRecord : recordAB) {
                    CreateRecordBody.DataAB dataAB = new CreateRecordBody.DataAB();
                    dataAB.setSpeaker(fileRecord.getSpeaker());
                    dataAB.setTime(fileRecord.getTime());
                    data1.add(dataAB);

                }
                for (FileRecord fileRecord : recordBC) {
                    CreateRecordBody.DataBC dataBC = new CreateRecordBody.DataBC();
                    dataBC.setContent(fileRecord.getContent());
                    dataBC.setTime(fileRecord.getTime());
                    data2.add(dataBC);
                }

                createRecordBody.setData1(data1);
                createRecordBody.setData2(data2);
                createRecord(createRecordBody);

            } else {
                Toast.makeText(getContext(), "Các File nhập vào không trùng khớp", Toast.LENGTH_SHORT).show();
            }
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
