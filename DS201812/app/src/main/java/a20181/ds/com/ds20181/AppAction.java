package a20181.ds.com.ds20181;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public enum AppAction {

    ADD_RECORD("add_record"),
    UNDO_CLICK("undo_record"),
    REDO_CLICK("redo_record"),
    ADD_CLICK("import_record"),
    SHOW_ICON_UNDO_REDO("redo_record"),
    FINISH_STREAM("finish_stream"),
    RESPONSE_DATA_WHO("rp_data_who"),
    RESPONSE_DATA_WHAT("rp_data_what"),
    FINISH_RECORD("finish_record");

    public String value;

    AppAction(String value) {
        this.value = value;
    }

    public String getExtraData() {
        return extraData;
    }

    private String extraData;

    public AppAction setData(Object extraData) {
        this.extraData = new Gson().toJson(extraData);
        return this;
    }

    public <T> T getData(Class<T> target) {
        return new Gson().fromJson(extraData, target);
    }

    public <T> List<T> getDataList() {
        return new Gson().fromJson(extraData, new TypeToken<List<T>>() {
        }.getType());
    }


    @Override
    public String toString() {
        return value;
    }
}
