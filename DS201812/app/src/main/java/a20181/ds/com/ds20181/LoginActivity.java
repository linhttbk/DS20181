package a20181.ds.com.ds20181;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import a20181.ds.com.ds20181.R;
import a20181.ds.com.ds20181.customs.DisableTouchView;
import a20181.ds.com.ds20181.fragments.LoginFragment;
import a20181.ds.com.ds20181.fragments.SignUpFragment;
import a20181.ds.com.ds20181.listeners.LoginCallback;
import a20181.ds.com.ds20181.listeners.SignUpCallback;
import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.ResponseLogin;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginCallback, SignUpCallback {
    @BindView(R.id.layoutProgress)
    DisableTouchView layoutProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();

    }

    private void init() {
        LoginFragment loginFragment = LoginFragment.newInstance();
        loginFragment.setCallback(this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.root, loginFragment);
        ft.commit();

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
        ft.replace(R.id.root, fragment).addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onCreateAccountClick() {
        SignUpFragment signUpFragment = SignUpFragment.newInstance();
        signUpFragment.setCallback(this);
        replaceFragment(signUpFragment);
    }

    @Override
    public void onLoginClick(String username, String password) {
        layoutProgress.setVisibility(View.VISIBLE);
        Call<ResponseLogin> result;
        if (Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            result = AppClient.getAPIService().loginWithEmail(username, password);
        } else {
            result = AppClient.getAPIService().loginWithUid(username, password);

        }
        result.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                layoutProgress.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseLogin responses = response.body();
                        Toast.makeText(LoginActivity.this, responses.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                layoutProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void signUp(String username, String email, String password) {
        layoutProgress.setVisibility(View.VISIBLE);
        AppClient.getAPIService().signUp(username, email, password).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                layoutProgress.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        BaseResponse responses = response.body();
                        Toast.makeText(LoginActivity.this, responses.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                layoutProgress.setVisibility(View.GONE);
            }
        });
    }

}
