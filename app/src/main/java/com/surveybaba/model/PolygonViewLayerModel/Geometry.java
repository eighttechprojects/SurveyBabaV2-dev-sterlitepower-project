package com.surveybaba.model.PolygonViewLayerModel;

import java.util.ArrayList;

public class Geometry {

    private String type;
    private ArrayList<ArrayList<ArrayList<Double>>> coordinates;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getCoordinates() {
        return coordinates;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(ArrayList<ArrayList<ArrayList<Double>>> coordinates) {
        this.coordinates = coordinates;
    }
}
