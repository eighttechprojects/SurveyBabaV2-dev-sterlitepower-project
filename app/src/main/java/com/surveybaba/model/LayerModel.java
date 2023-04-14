package com.surveybaba.model;

import java.util.ArrayList;

public class LayerModel {

    private String viewType;

    private ArrayList<ProjectLayerModel> list;

    public LayerModel(String viewType, ArrayList<ProjectLayerModel> list) {
        this.viewType = viewType;
        this.list = list;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public ArrayList<ProjectLayerModel> getList() {
        return list;
    }

    public void setList(ArrayList<ProjectLayerModel> list) {
        this.list = list;
    }
}
