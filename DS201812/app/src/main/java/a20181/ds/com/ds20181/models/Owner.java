package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import a20181.ds.com.ds20181.AppConstant;

public class Owner implements AppConstant {

    @SerializedName("id")
    private String id;
    @SerializedName("per")
    private int per;

    public Owner(String id, int per) {
        this.id = id;
        this.per = per;
    }

    public String getId() {
        return id;
    }

    public int getPer() {
        return per;
    }

    public boolean isWritable() {
        return per == PERMISSION_WRITE;
    }
}
