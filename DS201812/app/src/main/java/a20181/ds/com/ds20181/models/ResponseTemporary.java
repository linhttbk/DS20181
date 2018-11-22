package a20181.ds.com.ds20181.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseTemporary {
    @SerializedName("temporary_who")
    private List<CreateRecordBody.DataAB> dataABS;

    public List<CreateRecordBody.DataAB> getDataABS() {
        if (dataABS == null) return new ArrayList<>();
        return dataABS;
    }

    @SerializedName("temporary_what")
    private List<CreateRecordBody.DataBC> dataBCS;

    public List<CreateRecordBody.DataBC> getDataBCS() {
        if (dataBCS == null) return new ArrayList<>();
        return dataBCS;
    }
}
