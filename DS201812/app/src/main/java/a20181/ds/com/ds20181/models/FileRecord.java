package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class FileRecord {
    @SerializedName("_id")
    private String id;
    @SerializedName("fileId")
    private String fileId;
    @SerializedName("speaker")
    private String speaker;
    @SerializedName("content")
    private String content;
    @SerializedName("time")
    private int time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
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

    public String getFileId() {
        return fileId;
    }
}
