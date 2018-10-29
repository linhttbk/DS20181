package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import a20181.ds.com.ds20181.models.BaseResponse;

public class ResponseLogin extends BaseResponse {

    @SerializedName("user")
    private UserResponseLogin user;

    public UserResponseLogin getUser() {
        return user;
    }
}
