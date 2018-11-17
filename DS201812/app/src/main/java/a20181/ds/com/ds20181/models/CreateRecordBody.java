package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateRecordBody {
    @SerializedName("data1")
    private List<DataAB> data1;
    @SerializedName("data2")
    private List<DataBC> data2;

    public  List<DataAB> getData1() {
        return data1;
    }

    public void setData1( List<DataAB> data1) {
        this.data1 = data1;
    }

    public List<DataBC> getData2() {
        return data2;
    }

    public void setData2( List<DataBC> data2) {
        this.data2 = data2;
    }

    public static class DataAB {
        @SerializedName("speaker")
        private String speaker;
        @SerializedName("time")
        private long time;

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
   public static class  DataBC {
        @SerializedName("content")
        private String content;
        @SerializedName("time")
        private long time;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

}

}