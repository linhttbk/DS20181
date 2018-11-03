package a20181.ds.com.ds20181.services;


import java.util.List;

import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.ResponseLogin;
import a20181.ds.com.ds20181.models.TestRes;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("test")
    Call<TestRes> test();

    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginWithUid(@Field("username") String username, @Field("password") String password);


    @POST("signup")
    @FormUrlEncoded
    Call<BaseResponse> signUp(@Field("username") String username, @Field("email") String email, @Field("password") String password);
    @Headers({"Content-Type: application/json"})
    @GET("file")
    Observable<List<FileFilm>> getAllCreatorFile(@Header("Cookie") String cookie, @Query("creator") String creatorId);

    @Headers({"Content-Type: application/json"})
    @GET("file")
    Observable<List<FileFilm>> getAllOwnerFile(@Header("Cookie") String cookie,@Query("owner") String ownerId);

    @Headers({"Content-Type: application/json"})
    @GET("file")
    Observable<List<FileFilm>> getAllUser(@Header("Cookie") String cookie,@Query("owner") String ownerId);
}

