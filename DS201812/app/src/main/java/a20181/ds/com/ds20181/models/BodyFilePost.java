package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BodyFilePost {
    @SerializedName("name")
    private String name;
    @SerializedName("owners")
    private List<String> owners = new ArrayList<>();
    @SerializedName("creatAt")
    private long creatAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public long getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(long creatAt) {
        this.creatAt = creatAt;
    }
}
