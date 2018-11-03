package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.adapters.UserShareAdapter;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

public class CreateFileFragment extends BaseFragment {
    @BindView(R.id.tagLayout)
    TagContainerLayout tagLayout;
    @BindView(R.id.autoComplete)
    AutoCompleteTextView autoCompleteTextView;
    @BindView(R.id.edtNameFile)
    EditText edtNameFile;
    UserShareAdapter adapter;

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
        List<User> users = new ArrayList<>();
        for (int ii = 0; ii < 8; ii++) {
            User user = new User();
            user.setName("Linhtt" + ii );
            users.add(user);
        }
        adapter = new UserShareAdapter(getContext(), 0,users);

        autoCompleteTextView.setAdapter(adapter);
        dummy();

    }

    private void dummy() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tags.add("Linhtt" + i);
        }
        tagLayout.setTags(tags);
    }
}
