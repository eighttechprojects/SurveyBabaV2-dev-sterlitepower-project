package com.surveybaba.model.PolylineViewLayerModel;

import java.util.ArrayList;

public class Geometry {

    private String type;
    private ArrayList<ArrayList<Double>> coordinates;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public ArrayList<ArrayList<Double>> getCoordinates() {
        return coordinates;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(ArrayList<ArrayList<Double>> coordinates) {
        this.coordinates = coordinates;
    }
}
