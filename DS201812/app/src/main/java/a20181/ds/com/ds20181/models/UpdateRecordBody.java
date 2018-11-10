package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

public class UpdateRecordBody {
    @SerializedName("options")
    private Options options;

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public static class Options {
        @SerializedName("fileID")
        private String fileID;
        @SerializedName("speaker")
        private String speaker;
        @SerializedName("time")
        private int time;
        @SerializedName("content")
        private String content;

        public Options(String fileID, String speaker, int time, String content) {
            this.fileID = fileID;
            this.speaker = speaker;
            this.time = time;
            this.content = content;
        }
    }
}
