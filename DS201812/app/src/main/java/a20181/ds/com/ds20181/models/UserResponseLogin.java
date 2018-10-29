package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class UserResponseLogin {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("username")

    private String username;
    @SerializedName("email")

    private String email;
    @SerializedName("enable")

    private String enable;
    @SerializedName("time")

    private String time;
    @SerializedName("token")

    private String token;

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getEnable() {
        return enable;
    }

    public String getTime() {
        return time;
    }

    public String getToken() {
        return token;
    }
}
