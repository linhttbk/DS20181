package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class FileRecord {
    @SerializedName("fileId")
    private String id;
    @SerializedName("speaker")
    private String userId;
    @SerializedName("content")
    private String content;
    @SerializedName("titme")
    private int time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
