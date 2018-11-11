package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> userActives = new ArrayList<>();

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

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public void setUserActives(List<String> userActives) {
        this.userActives = userActives;
    }

    public List<String> getUserActives() {
        return userActives;
    }

    public void addUserActive(String userName){
        userActives.add(userName);
    }

    public void deleteUserActive(String userName){
//        for (int i = 0; i < userActives.size(); i++){
//            if (userActives.get(i).equals(userName)){
//                userActives.remove(i);
//                break;
//            }
//        }
        userActives.remove(userName);
    }
}
