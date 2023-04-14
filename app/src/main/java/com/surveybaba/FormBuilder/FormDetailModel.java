package com.surveybaba.FormBuilder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FormDetailModel {

    int uniqueFormId;
    String latitude, longitude;

    @SerializedName("data")
    private ArrayList<FormDetailData> data;

    public ArrayList<FormDetailData> getData() {
        return data;
    }

    public void setData(ArrayList<FormDetailData> data) {
        this.data = data;
    }

    public int getUniqueFormId() {
        return uniqueFormId;
    }

    public void setUniqueFormId(int uniqueFormId) {
        this.uniqueFormId = uniqueFormId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
