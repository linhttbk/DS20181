package a20181.ds.com.ds20181;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import a20181.ds.com.ds20181.customs.DisableTouchView;
import a20181.ds.com.ds20181.fragments.LoginFragment;
import a20181.ds.com.ds20181.fragments.SignUpFragment;
import a20181.ds.com.ds20181.listeners.LoginCallback;
import a20181.ds.com.ds20181.listeners.SignUpCallback;
import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.ResponseLogin;
import a20181.ds.com.ds20181.models.TestRes;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static a20181.ds.com.ds20181.AppConstant.CODE_200;

public class LoginActivity extends AppCompatActivity implements LoginCallback, SignUpCallback,AppConstant {
    @BindView(R.id.layoutProgress)
    DisableTouchView layoutProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
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
        AppClient.getAPIService().loginWithUid(username, password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                layoutProgress.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseLogin responses = response.body();

                        if (responses!=null && responses.getCode() == CODE_200) {
                            User user = response.body().getUser();
                            Headers headers = response.headers();
                            String cookie = headers.get("set-cookie");
                            if (user != null) {
                                user.setCookie(cookie);
                                app.setCurrentUser(user);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, responses.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                t.printStackTrace();
                layoutProgress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
