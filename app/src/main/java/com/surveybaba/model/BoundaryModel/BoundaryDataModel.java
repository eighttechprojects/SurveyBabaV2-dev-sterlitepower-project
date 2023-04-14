package com.surveybaba.model.BoundaryModel;

import java.util.ArrayList;

public class BoundaryDataModel {

    private ArrayList<ArrayList<Double>> geodata;
    private String type;

    public ArrayList<ArrayList<Double>> getGeodata() {
        return geodata;
    }

    public void setGeodata(ArrayList<ArrayList<Double>> geodata) {
        this.geodata = geodata;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
