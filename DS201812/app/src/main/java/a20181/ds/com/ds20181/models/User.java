package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String userId;

    @SerializedName("role")
    private int role;

    @SerializedName("name")
    private String name;

    //    @SerializedName("token")
    private String cookie;

    public String getUserId() {
        return userId;
    }

    public int getRole() {
        return role;
    }



    public String getName() {
        return name;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
