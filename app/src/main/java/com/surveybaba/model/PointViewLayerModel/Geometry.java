package com.surveybaba.model.PointViewLayerModel;

import java.util.ArrayList;

public class Geometry {

    private String type;
    private ArrayList<Double> coordinates;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public ArrayList<Double> getCoordinates() {
        return coordinates;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(ArrayList<Double> coordinates) {
        this.coordinates = coordinates;
    }
}
