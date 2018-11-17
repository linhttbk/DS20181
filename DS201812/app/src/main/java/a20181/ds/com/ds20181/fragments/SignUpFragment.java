package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.BaseFragment;
import a20181.ds.com.ds20181.listeners.SignUpCallback;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpFragment extends BaseFragment {
    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_username)
    EditText inputUsername;
    @BindView(R.id.input_password)
    EditText inputPassword;
    private SignUpCallback callback;

    public static SignUpFragment newInstance() {
        Bundle args = new Bundle();
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.sign_up_fragment;
    }


    @Override
    public void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        String username = inputUsername.getText().toString();
        String name = inputName.getText().toString();
        String password = inputPassword.getText().toString();
        if (validate(username, name, password)) {
            if (callback != null) callback.signUp(username, name, password);
        }
    }

    public boolean validate(String username, String name, String password) {
        boolean valid = true;
        if (username.isEmpty() || StringUtils.isContainWhiteSpace(username) || !StringUtils.isAllCharacter(username)) {
            inputUsername.setError("Tài khoản không được để trống, là chuỗi ký tự không chứa khoảng trắng!");
            valid = false;
        }
        if (name.isEmpty()) {
            inputName.setError("Tên không được để trống!");
            valid = false;
        } else {
            inputName.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            inputPassword.setError("mật khẩu phải từ 6 đến 10 ký tự");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    public void setCallback(SignUpCallback callback) {
        this.callback = callback;
    }

    @OnClick(R.id.back_login)
    public void backToLogin() {
        getActivity().onBackPressed();
    }
}
