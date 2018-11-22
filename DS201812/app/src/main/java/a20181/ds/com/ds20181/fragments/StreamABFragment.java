package a20181.ds.com.ds20181.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Result;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.BuildConfig;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.BaseRecyclerViewAdapter;
import a20181.ds.com.ds20181.adapters.ContentStreamABAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.CreateRecordBody;
import a20181.ds.com.ds20181.models.CreateRecordBody.*;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.FileUtil;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.socket.emitter.Emitter;
import retrofit2.Response;

public class StreamABFragment extends BaseFragment implements BaseRecyclerViewAdapter.ItemClickListener, AppConstant {
    @BindView(R.id.rcvList)
    RecyclerView rcvList;
    ContentStreamABAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    String streamPath = EMPTY;
    private String fileId = EMPTY;

    public static StreamABFragment newInstance(String fileId) {

        Bundle args = new Bundle();

        StreamABFragment fragment = new StreamABFragment();
        fragment.fileId = fileId;
        fragment.setArguments(args);
        return fragment;
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSocket().on(EVENT_IMPORT_WHO, importWho);
    }

    private Emitter.Listener importWho = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
    //    private void initTemporyRecords() {
//        if (app.getCurrentUser() == null) return;
//        if (getActivity() != null)
//            ((MainActivity) getActivity()).showLoading(true);
//        Disposable disposable = AppClient.getAPIService().gettAllTemporyWho(app.getCookie(), fileId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<DataAB>>() {
//                    @Override
//                    public void accept(List<DataAB> dataABS) throws Exception {
//                        if (getActivity() != null)
//                            ((MainActivity) getActivity()).showLoading(false);
//                        adapter.add(dataABS);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        if (getActivity() != null) {
//                            ((MainActivity) getActivity()).showLoading(false);
//                            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.e("accept: ", throwable.getMessage());
//                        }
//                    }
//                });
//        compositeDisposable.add(disposable);
//    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @OnClick(R.id.btnImportFile)
    public void onImportClick() {
        requestPermission();
    }

    private void requestPermission() {
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            // write your logic code if permission already granted
            createTemFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile) {
                        // write your logic here
                        createTemFile();
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content),
                                "Please Grant Permissions to upload profile photo",
                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(
                                                new String[]{Manifest.permission
                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                PERMISSIONS_MULTIPLE_REQUEST);
                                    }
                                }).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                streamPath = FileUtil.getRealPathFromURI(getContext(), uri);
                // File file = new File(streamPath);
                try {
                    StringBuilder text = new StringBuilder();
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    executeJsonTextFile(text.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();


                }
            }
        }
    }

    private void executeJsonTextFile(String text) {
        Disposable disposable = Observable.just(text).map(new Function<String, Object>() {

            @Override
            public Object apply(String s) throws Exception {
                List<DataAB> results = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("who");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        String speaker = array.getJSONObject(i).getString("speaker");
                        // String af = array.getJSONObject(i).getString("af");
                        int time = array.getJSONObject(i).getInt("time");
                        if (!StringUtils.isEmpty(speaker)) {
                            DataAB record = new DataAB();
                            record.setSpeaker(speaker);
                            record.setTime(time);
                            results.add(record);
                        }
                    }
                }
                return results;
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        if (o instanceof ArrayList) {
                            List<DataAB> records = (ArrayList) o;
                            if (!records.isEmpty()) {
                                CreateRecordBody createRecordBody = new CreateRecordBody();
                                createRecordBody.setData1(records);
                                createRecordBody.setTime(System.currentTimeMillis());

                            }
                            adapter.add(records);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), "Định dạng file không hợp lệ", Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Subscribe
    public void onAppAction(AppAction appAction) {
        if (appAction == AppAction.ADD_RECORD) {
//            FileRecord record = appAction.getData(FileRecord.class);
//            adapter.addItem(record);
            adapter.clear();
        } else if (appAction == AppAction.RESPONSE_DATA_WHO) {
            String data = appAction.getExtraData();

            List<DataAB> dataABS = new Gson().fromJson(data, new TypeToken<List<DataAB>>() {
            }.getType());
            if (adapter != null) adapter.add(dataABS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) compositeDisposable.dispose();
        getSocket().off(EVENT_IMPORT_WHO);

    }

    public List<DataAB> getRecordAB() {
        return adapter.getAll();
    }

    private void uploadWhat(final List<DataAB> dataABS) {
        if (app.getCurrentUser() == null || getActivity() == null) return;
        ((MainActivity) getActivity()).showLoading(true);
        CreateRecordBody createRecordBody = new CreateRecordBody();
        createRecordBody.setData1(dataABS);
        createRecordBody.setData2(new ArrayList<DataBC>());
        createRecordBody.setTime(System.currentTimeMillis());
        Disposable disposable = AppClient.getAPIService().importWhatWhoData(app.getCookie(), fileId, createRecordBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<Void>>() {
                    @Override
                    public void accept(Response<Void> voidResponse) throws Exception {
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            if (voidResponse.isSuccessful() && (voidResponse.code() == CODE_200 || voidResponse.code() == CODE_204)) {
                                adapter.add(dataABS);
                            }
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

    private void createTemFile() {
        File photoDirectory = new File(Environment.getExternalStorageDirectory(), IMAGE_PATH);
        if (!photoDirectory.exists() && !photoDirectory.mkdirs()) {
            return;
        }
        File stream = new File(photoDirectory, String.format(STREAM_FILE_NAME,
                new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())));
        streamPath = stream.getPath();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri streamUri = FileProvider.getUriForFile(getActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                stream);
        intent.setType("text/plain");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, streamUri);
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
