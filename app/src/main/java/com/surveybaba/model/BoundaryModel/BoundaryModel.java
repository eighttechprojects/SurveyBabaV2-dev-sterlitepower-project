package com.surveybaba.model.BoundaryModel;

import java.util.ArrayList;

public class BoundaryModel {
    private ArrayList<BoundaryDataModel> data;
    private String msg;

    public ArrayList<BoundaryDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<BoundaryDataModel> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
