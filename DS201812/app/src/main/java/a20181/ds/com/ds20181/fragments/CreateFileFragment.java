package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.UserShareAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.BodyFile;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.FileResponse;
import a20181.ds.com.ds20181.models.ListUserResponse;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import a20181.ds.com.ds20181.utils.ListUtil;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CreateFileFragment extends BaseFragment implements AppConstant {
    @BindView(R.id.tagLayout)
    TagContainerLayout tagLayout;
    @BindView(R.id.autoComplete)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.edtNameFile)
    EditText edtNameFile;
    UserShareAdapter adapter;
    private List<User> shareList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static CreateFileFragment newInstance() {
        Bundle args = new Bundle();
        CreateFileFragment fragment = new CreateFileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.create_file_film_fragment;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        tagLayout.setEnableCross(true);
    }

    @Override
    public void initData() {
        super.initData();
        shareList = new ArrayList<>();
        if (app.getCurrentUser() == null) return;

        List<User> allUser = app.getListUser();
        if (allUser != null) {
            adapter = new UserShareAdapter(getContext(), 0, allUser);
            autoCompleteTextView.setAdapter(adapter);
        } else {
            adapter = new UserShareAdapter(getContext(), 0, new ArrayList<User>());
            autoCompleteTextView.setAdapter(adapter);
            ((MainActivity) getActivity()).showLoading(true);
            Disposable disposable = AppClient.getAPIService().getAllUser(app.getCookie())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ListUserResponse>() {
                        @Override
                        public void accept(ListUserResponse listUserResponse) throws Exception {
                            ((MainActivity) getActivity()).showLoading(false);
                            List<User> users = listUserResponse.getData();
                            adapter.replace(users);
                            app.setListUser(users);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ((MainActivity) getActivity()).showLoading(false);
                        }
                    });
            compositeDisposable.add(disposable);
        }

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User item = adapter.getItem(i);
                if (item != null) {
                    tagLayout.addTag(item.getName());
                    shareList.add(item);

                }
            }
        });

        tagLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                tagLayout.removeTag(position);
                if (position > 0 && position < shareList.size()) {
                    shareList.remove(position);
                }
            }
        });

    }

    @OnClick(R.id.btnAddFile)
    public void addFile() {
        User currentUser = app.getCurrentUser();
        if (currentUser == null) return;
        String nameFile = edtNameFile.getText().toString();

        if (StringUtils.isEmpty(nameFile)) {
            Toast.makeText(getContext(), "Bạn chưa nhập tên File", Toast.LENGTH_SHORT).show();
            return;
        }
        ((MainActivity) getActivity()).showLoading(true);
        List<String> shares = new ArrayList<>();
        if (!ListUtil.isEmpty(shareList)) {
            for (User user : shareList) {
                if (!shares.contains(user.getUserId())) shares.add(user.getUserId());
            }
        }
        BodyFile film = new BodyFile();
        film.setName(nameFile);
        film.setCreateAt(System.currentTimeMillis());
        film.setOwners(shares);
        JsonObject file = generateRegistrationRequest(nameFile, System.currentTimeMillis(), shares);
        Disposable disposable = AppClient.getAPIService().createFile(app.getCookie(), file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FileFilm>() {
                    @Override
                    public void accept(FileFilm fileFilm) throws Exception {
                        Log.e("accept: ", fileFilm.getId());
                        if (getActivity() != null) {
                            ((MainActivity) getActivity()).showLoading(false);
                            ((MainActivity) getActivity()).switchFragment(RecordStreamFragment.newInstance(fileFilm.getId()));
                        }
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

    private static JsonObject generateRegistrationRequest(String nameFile, long creatAt, List<String> shares) {
        try {
            JSONObject subJsonObject = new JSONObject();
            subJsonObject.put("name", nameFile);
            subJsonObject.put("creatAt", creatAt);
            JsonArray array = new JsonArray();
            for (String id : shares) {
                array.add(id);
            }

            subJsonObject.put("owners", array);
            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject) jsonParser.parse(subJsonObject.toString());
            return gsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null) compositeDisposable.dispose();
    }
}
