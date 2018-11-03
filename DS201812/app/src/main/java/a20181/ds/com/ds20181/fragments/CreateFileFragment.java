package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.MainActivity;
import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.UserShareAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.ListUserResponse;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
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
            }
        });
    }

    @Override
    public void initData() {
        super.initData();

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
                if (item != null) tagLayout.addTag(item.getName());
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(compositeDisposable!=null) compositeDisposable.dispose();
    }
}
