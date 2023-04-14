package com.surveybaba.model.LayerMoveModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.surveybaba.model.ProjectLayerModel;
import java.util.ArrayList;

public class PolylineMoveModel {

    private int polylineIndex;
    private ArrayList<LatLng> currentPolylineLatLong;
    private int polylineColor;
    private String polylineType;
    private ProjectLayerModel projectLayerModel;
    private Polyline polyline;
    private Marker polylineMarker;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public int getPolylineIndex() {
        return polylineIndex;
    }

    public ArrayList<LatLng> getCurrentPolylineLatLong() {
        return currentPolylineLatLong;
    }

    public int getPolylineColor() {
        return polylineColor;
    }

    public String getPolylineType() {
        return polylineType;
    }

    public ProjectLayerModel getProjectLayerModel() {
        return projectLayerModel;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public Marker getPolylineMarker() {
        return polylineMarker;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setPolylineIndex(int polylineIndex) {
        this.polylineIndex = polylineIndex;
    }

    public void setCurrentPolylineLatLong(ArrayList<LatLng> currentPolylineLatLong) {
        this.currentPolylineLatLong = currentPolylineLatLong;
    }

    public void setPolylineColor(int polylineColor) {
        this.polylineColor = polylineColor;
    }

    public void setPolylineType(String polylineType) {
        this.polylineType = polylineType;
    }

    public void setProjectLayerModel(ProjectLayerModel projectLayerModel) {
        this.projectLayerModel = projectLayerModel;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public void setPolylineMarker(Marker polylineMarker) {
        this.polylineMarker = polylineMarker;
    }
}
