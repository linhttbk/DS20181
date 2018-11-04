package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import a20181.ds.com.ds20181.AppConstant;
import a20181.ds.com.ds20181.utils.StringUtils;

public class User {
    @SerializedName("id")
    private String userId;

    @SerializedName("name")
    private String name;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    @Override
    public String toString() {
        if (StringUtils.isEmpty(name)) return AppConstant.EMPTY;
        return name;
    }
}
