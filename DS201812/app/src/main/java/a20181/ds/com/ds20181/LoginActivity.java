package a20181.ds.com.ds20181;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import a20181.ds.com.ds20181.customs.DisableTouchView;
import a20181.ds.com.ds20181.fragments.LoginFragment;
import a20181.ds.com.ds20181.fragments.SignUpFragment;
import a20181.ds.com.ds20181.listeners.LoginCallback;
import a20181.ds.com.ds20181.listeners.SignUpCallback;
import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.ListUserResponse;
import a20181.ds.com.ds20181.models.ResponseLogin;
import a20181.ds.com.ds20181.models.SignUpBody;
import a20181.ds.com.ds20181.models.User;
import a20181.ds.com.ds20181.services.AppClient;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginCallback, SignUpCallback, AppConstant {
    @BindView(R.id.layoutProgress)
    DisableTouchView layoutProgress;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        User user = app.getCurrentUser();
        if (user == null) return;


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
//        AppClient.getAPIService().loginWithUid(username, password).enqueue(new Callback<ResponseLogin>() {
//            @Override
//            public void onResponse(Call<ResponseLogin> call, final Response<ResponseLogin> response) {
//                layoutProgress.setVisibility(View.GONE);
//                try {
//                    if (response.isSuccessful() && response.body() != null) {
//                        ResponseLogin responses = response.body();
//
//                        if (responses != null && responses.getCode() == CODE_200) {
//                            User user = response.body().getUser();
//                            Headers headers = response.headers();
//                            String cookie = headers.get("set-cookie");
//                            if (user != null) {
//                                user.setCookie(cookie);
//                                app.setCurrentUser(user);
//                                AppClient.getAPIService().getAllUser(cookie).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//                                        .subscribe(new Consumer<ListUserResponse>() {
//                                            @Override
//                                            public void accept(ListUserResponse listUserResponse) throws Exception {
//                                                layoutProgress.setVisibility(View.GONE);
//                                                Log.e( "accept: ",listUserResponse.getData().size() +"" );
//                                            }
//                                        }, new Consumer<Throwable>() {
//                                            @Override
//                                            public void accept(Throwable throwable) throws Exception {
//                                                Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            }
//                        } else {
//                            Toast.makeText(LoginActivity.this, responses.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    } else {
//                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseLogin> call, Throwable t) {
//                t.printStackTrace();
//                layoutProgress.setVisibility(View.GONE);
//                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
        Disposable disposable = AppClient.getAPIService().login(username, password)
                .flatMap(new Function<Response, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Response response) throws Exception {
                        if (response != null && response.isSuccessful() && response.body() != null) {
                            ResponseLogin responseLogin = (ResponseLogin) response.body();
                            User user = responseLogin.getUser();
                            Headers headers = response.headers();
                            String cookie = headers.get("set-cookie");
                            if (user != null) {
                                user.setCookie(cookie);
                                app.setCurrentUser(user);
                                Log.e("apply: ", "Login success");
                                return AppClient.getAPIService().getAllUser(cookie);
                            } else {
                                Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, R.string.msg_login_fail, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        return null;
                    }

                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        layoutProgress.setVisibility(View.GONE);
                        if (o != null && o instanceof ListUserResponse) {
                            List<User> users = ((ListUserResponse) o).getData();
                            app.setListUser(users);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(final Throwable throwable) throws Exception {
                        layoutProgress.setVisibility(View.GONE);

                    }
                });
        compositeDisposable.add(disposable);


    }

    @Override
    public void signUp(String username, String name, String password) {
        layoutProgress.setVisibility(View.VISIBLE);
        String realPass = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
        SignUpBody body = new SignUpBody(new SignUpBody.UserBody(username, realPass, name));
        AppClient.getAPIService().signUp(body).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                layoutProgress.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(LoginActivity.this, "Đăng ký thành công, chuyển về trang đăng nhập!", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                    } else {
                        Toast.makeText(LoginActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                layoutProgress.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
