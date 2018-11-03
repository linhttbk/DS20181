package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListUserResponse extends BaseResponse {
    @SerializedName("data")
    private List<User> data;

    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }
}
