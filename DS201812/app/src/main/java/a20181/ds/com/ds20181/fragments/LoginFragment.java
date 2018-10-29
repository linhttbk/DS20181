package a20181.ds.com.ds20181.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.listeners.LoginCallback;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;

    public void setCallback(LoginCallback callback) {
        this.callback = callback;
    }

    public LoginCallback callback;

    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.link_signup)
    public void onSignUpClick() {
        if (callback != null) callback.onCreateAccountClick();
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        String username = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (validate(username, password)) {
            if (callback != null) callback.onLoginClick(username, password);
        }

    }

    public boolean validate(String username, String password) {
        boolean valid = true;

        if (username.isEmpty()) {
            inputEmail.setError("please enter username or email!");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty()) {
            inputPassword.setError("please enter password!");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

}
