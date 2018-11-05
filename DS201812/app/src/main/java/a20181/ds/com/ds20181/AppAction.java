package a20181.ds.com.ds20181;

import com.google.gson.Gson;

public enum AppAction {

    ADD_RECORD("add_record"),
    FINISH_RECORD("finish_record");

    public String value;

    AppAction(String value) {
        this.value = value;
    }

    private String extraData;

    public AppAction setData(Object extraData) {
        this.extraData = new Gson().toJson(extraData);
        return this;
    }

    public <T> T getData(Class<T> target) {
        return new Gson().fromJson(extraData, target);
    }

    @Override
    public String toString() {
        return value;
    }
}
