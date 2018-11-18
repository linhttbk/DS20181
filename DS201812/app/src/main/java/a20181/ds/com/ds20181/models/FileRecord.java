package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import a20181.ds.com.ds20181.patterns.Command;
import a20181.ds.com.ds20181.patterns.CommandCallBack;

public class FileRecord implements Command {
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

    private CommandCallBack callBack;
    @SerializedName("userOn")
    private List<User> userActives = new ArrayList<>();

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

    public void setUserActives(List<User> userActives) {
        this.userActives = userActives;
    }

    public List<User> getUserActives() {
        if (userActives == null || userActives.isEmpty()) return new ArrayList<>();
        return userActives;
    }

    public void addUserActive(User user) {
        if (userActives != null && user != null) {
            for (int i = 0; i < userActives.size(); i++) {
                User active = userActives.get(i);
                if (active.getUserId().equals(user.getUserId())) return;
            }
            userActives.add(user);
        }
    }

    public void deleteUserActive(User user) {
        for (int i = 0; i < userActives.size(); i++){
            if (userActives.get(i).getName().equals(user.getName())){
                userActives.remove(i);
                break;
            }
        }
    }

    @Override
    public void undo() {
        if (callBack != null) callBack.undo(this);
    }

    @Override
    public void redo() {
        if (callBack != null) callBack.redo(this);
    }

    public void setCallBack(CommandCallBack callBack) {
        this.callBack = callBack;
    }
}
