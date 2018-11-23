package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class History {
    public String getAuthor() {
        return author;
    }

    public long getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    @SerializedName("author")
    private String author;
    @SerializedName("time")
    private long time;
    @SerializedName("message")
    private String message;
}
