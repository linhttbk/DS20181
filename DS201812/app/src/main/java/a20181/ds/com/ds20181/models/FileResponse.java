package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class FileResponse {
    @SerializedName("file")
    private FileFilm file;

    public FileFilm getFile() {
        return file;
    }
}
