package a20181.ds.com.ds20181.services;


import java.util.List;

import a20181.ds.com.ds20181.models.BaseResponse;
import a20181.ds.com.ds20181.models.BodyFile;
import a20181.ds.com.ds20181.models.CreateRecordBody;
import a20181.ds.com.ds20181.models.FileFilm;
import a20181.ds.com.ds20181.models.FileRecord;
import a20181.ds.com.ds20181.models.BodyFilePost;
import a20181.ds.com.ds20181.models.ListUserResponse;
import a20181.ds.com.ds20181.models.ResponseLogin;
import a20181.ds.com.ds20181.models.SignUpBody;
import a20181.ds.com.ds20181.models.UpdateRecordBody;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("login")
    @FormUrlEncoded
    Call<ResponseLogin> loginWithUid(@Field("username") String username, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<Response<ResponseLogin>> login(@Field("username") String username, @Field("password") String password);


    @POST("user")
    @FormUrlEncoded
    Call<BaseResponse> signUp(@Field("username") String username, @Field("name") String name, @Field("password") String password);

    @POST("user")
    Call<BaseResponse> signUp(@Body SignUpBody body );

    @Headers({"Content-Type: application/json"})
    @GET("file")
    Observable<List<FileFilm>> getAllCreatorFile(@Header("Cookie") String cookie, @Query("creator") String creatorId);

    @Headers({"Content-Type: application/json"})
    @GET("file")
    Observable<List<FileFilm>> getAllOwnerFile(@Header("Cookie") String cookie, @Query("owner") String ownerId);

    @Headers({"Content-Type: application/json"})
    @GET("record")
    Observable<List<FileRecord>> getRecordFile(@Header("Cookie") String cookie, @Query("fileId") String fileId);


    @Headers({"Content-Type: application/json"})
    @GET("get_all_user")
    Observable<ListUserResponse> getAllUser(@Header("Cookie") String cookie);


    @Headers({"Content-Type: application/json"})
    @POST("file")
    @FormUrlEncoded
    Observable<FileFilm> createFile(@Header("Cookie") String cookie, @Field("name") String fileName, @Field("createAt") long createAt, @Field("owners[]") List<String> owner);

    @Headers({"Content-Type: application/json"})
    @POST("file")
    Observable<FileFilm> createFile(@Header("Cookie") String cookie, @Body BodyFilePost file);

    @Headers({"Content-Type: application/json"})
    @POST("record/{fileID}")
    Observable<FileRecord> addRecord(@Header("Cookie") String cookie, @Path("fileID") String fileId, @Body CreateRecordBody recordBody);

    @Headers({"Content-Type: application/json"})
    @POST("record/list-data/{fileID}")
    Observable<List<FileRecord>> importRecord(@Header("Cookie") String cookie, @Path("fileID") String fileId, @Body CreateRecordBody recordBody);

    @Headers({"Content-Type: application/json"})
    @PUT("record/{id}")
    Observable<FileRecord> updateRecord(@Header("Cookie") String cookie, @Path("id") String recordId, @Body UpdateRecordBody recordBody);

    @Headers({"Content-Type: application/json"})
    @DELETE("file/{id}")
    Observable<Response<Void>> deleteFile(@Header("Cookie") String cookie, @Path("id") String idFile);

    @Headers({"Content-Type: application/json"})
    @DELETE("record/{id}")
    Observable<BaseResponse> deleteRecord(@Header("Cookie") String cookie, @Path("id") String idRecord);


}

