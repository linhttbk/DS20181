package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import a20181.ds.com.ds20181.utils.StringUtils;

public class FileFilm {
    private boolean isHeader = false;
    @SerializedName("_id")

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    @SerializedName("name")
    private String name;
    @SerializedName("creator")
    private String creator;
    @SerializedName("createAt")
    private long createAt;

    public List<Owner> getOwners() {
        return owners;
    }

    @SerializedName("owners")
    private List<Owner> owners;
    @SerializedName("description")
    private String description;

    @SerializedName("date")
    private long date;

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isCreator(String userId) {
        if (StringUtils.isEmpty(userId)) return false;
        return userId.equals(creator);
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public boolean isWriteAble(String userId) {
        if (owners == null || owners.isEmpty() || StringUtils.isEmpty(userId)) return false;
        for (Owner owner : owners) {
            if (owner.getId().equals(userId))
                return owner.isWritable();
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public long getDate() {
        return date;
    }

    public static class History {
        @SerializedName("time")
        private long time;
        @SerializedName("message")
        private String message;
        @SerializedName("author")
        private String author;
    }
}
