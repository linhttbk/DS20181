package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BodyFilePost {
    @SerializedName("name")
    private String name;
    @SerializedName("owners")
    private List<Owner> owners = new ArrayList<>();
    @SerializedName("createAt")
    private long createAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
