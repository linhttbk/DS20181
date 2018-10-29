package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
