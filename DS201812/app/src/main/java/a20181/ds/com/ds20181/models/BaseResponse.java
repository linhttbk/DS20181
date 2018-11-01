package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
