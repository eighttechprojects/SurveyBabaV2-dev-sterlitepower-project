package com.surveybaba.model.PolygonViewLayerModel;

import java.util.ArrayList;

public class PolygonViewLayerModel {

    private String type;
    private ArrayList<Feature> features;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setType(String type) {
        this.type = type;
    }

    public void setFeatures(ArrayList<Feature> features) {
        this.features = features;
    }
}
