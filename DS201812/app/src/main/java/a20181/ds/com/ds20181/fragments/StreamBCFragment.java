package a20181.ds.com.ds20181.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppAction;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.ContentStreamBCAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.FileRecord;
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

public class StreamBCFragment extends BaseFragment  {
    @BindView(R.id.rcvList)
    RecyclerView rcvList;
    ContentStreamBCAdapter adapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

    @OnClick(R.id.btnImportFile)
    public void onImportClick() {
        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (getActivity() != null)
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,},
                        REQUEST_READ_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            startActivityForResult(intent, FILE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }
        } else {
            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                String filePath = FileUtil.getRealPathFromURI(getContext(), uri);
                if (filePath != null) {
                    File file = new File(filePath);
                    StringBuilder text = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = br.readLine()) != null) {
                            text.append(line);
                            text.append('\n');
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    executeJsonTextFile(text.toString());
                }
            }
        }
    }

    private void executeJsonTextFile(String text) {
        Disposable disposable = Observable.just(text).map(new Function<String, Object>() {

            @Override
            public Object apply(String s) throws Exception {
                List<FileRecord> results = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("what");
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        String content = array.getJSONObject(i).getString("content");
                        int time = array.getJSONObject(i).getInt("time");
                        if (!StringUtils.isEmpty(content)) {
                            FileRecord record = new FileRecord();
                            record.setContent(content);
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
                        if(o instanceof  ArrayList){
                            List<FileRecord> records = (ArrayList)o;
                            adapter.set(records);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Subscribe
    public void onAppAction(AppAction appAction) {
        if(appAction == AppAction.ADD_RECORD) {
//            FileRecord record = appAction.getData(FileRecord.class);
//            adapter.addItem(record);
            adapter.clear();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
    public List<FileRecord> getRecordBC(){
        return adapter.getAll();
    }
}
