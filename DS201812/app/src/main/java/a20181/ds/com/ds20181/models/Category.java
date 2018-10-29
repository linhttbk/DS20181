package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }

    @SerializedName("title")

    private String title;
    @SerializedName("image")
    private String thumb;
}
