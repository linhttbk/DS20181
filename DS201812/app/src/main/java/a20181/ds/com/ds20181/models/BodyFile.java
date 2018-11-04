package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.utils.StringUtils;

public class BodyFile {


    public String getName() {
        return name;
    }




    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("createAt")
    private long createAt;
    @SerializedName("owners")
    private List<String> owners = new ArrayList<>();




    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public void setName(String name) {
        this.name = name;
    }
}
