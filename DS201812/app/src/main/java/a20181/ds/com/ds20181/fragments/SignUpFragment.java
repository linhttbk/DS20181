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
import a20181.ds.com.ds20181.listeners.SignUpCallback;
import a20181.ds.com.ds20181.utils.StringUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpFragment extends Fragment {
    @BindView(R.id.input_email)
    EditText inputEmail;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        if (validate(username, email, password)) {
            if (callback != null) callback.signUp(username, email, password);
        }
    }

    public boolean validate(String username, String email, String password) {
        boolean valid = true;
        if (username.isEmpty() || StringUtils.isContainWhiteSpace(username) || !StringUtils.isAllCharacter(username)) {
            inputUsername.setError("username must be alphanumeric characters");
            valid = false;
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            inputPassword.setError("between 6 and 10 alphanumeric characters");
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
