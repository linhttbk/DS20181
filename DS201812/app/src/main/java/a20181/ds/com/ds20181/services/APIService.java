package a20181.ds.com.ds20181.services;

import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.CategoryResponse;
import a20181.ds.com.ds20181.models.ResponseLogin;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginWithUid(@Field("username") String username, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginWithEmail(@Field("email") String email, @Field("password") String password);

    @POST("signup")
    @FormUrlEncoded
    Call<BaseResponse> signUp(@Field("username") String username, @Field("email") String email, @Field("password") String password);

    @GET("categories")
    Call<CategoryResponse> getAllCategories();
}

