package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateRecordBody {
    @SerializedName("dataArray1")
    private List<DataAB> dataArray1;
    @SerializedName("dataArray2")
    private List<DataBC> dataArray2;
    @SerializedName("time")
    private long time;

    public List<DataAB> getData1() {
        return dataArray1;
    }

    public void setData1(List<DataAB> data1) {
        this.dataArray1 = data1;
    }

    public List<DataBC> getData2() {
        return dataArray2;
    }

    public void setData2(List<DataBC> data2) {
        this.dataArray2 = data2;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class DataAB {
        @SerializedName("speaker")
        private String speaker;
        @SerializedName("time")
        private long time;
        @SerializedName("afr")
        private String afr;

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

        public String getAfr() {
            return afr;
        }

        public void setAfr(String afr) {
            this.afr = afr;
        }
    }

    public static class DataBC {
        @SerializedName("content")
        private String content;
        @SerializedName("time")
        private long time;
        @SerializedName("afr")
        private String afr;

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

        public String getAfr() {
            return afr;
        }

        public void setAfr(String afr) {
            this.afr = afr;
        }
    }

}