package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String userId;

    @SerializedName("name")
    private String name;

    private String cookie;

    public String getUserId() {
        return userId;
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
