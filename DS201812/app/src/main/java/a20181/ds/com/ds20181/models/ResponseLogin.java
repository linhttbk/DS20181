package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin extends BaseResponse {

    @SerializedName("data")
    private User user;

    public User getUser() {
        return user;
    }
}
