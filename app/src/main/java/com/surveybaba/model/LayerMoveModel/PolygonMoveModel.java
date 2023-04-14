package com.surveybaba.model.LayerMoveModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.surveybaba.model.ProjectLayerModel;
import java.util.ArrayList;
public class PolygonMoveModel {

    private int polygonIndex;
    private ArrayList<LatLng> currentPolygonLatLong;
    private int polygonStrokeColor;
    private int polygonFillColor;
    private ProjectLayerModel projectLayerModel;
    private Polygon polygon;
    private Marker polygonMarker;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public int getPolygonIndex() {
        return polygonIndex;
    }

    public ArrayList<LatLng> getCurrentPolygonLatLong() {
        return currentPolygonLatLong;
    }

    public int getPolygonStrokeColor() {
        return polygonStrokeColor;
    }

    public int getPolygonFillColor() {
        return polygonFillColor;
    }

    public ProjectLayerModel getProjectLayerModel() {
        return projectLayerModel;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public Marker getPolygonMarker() {
        return polygonMarker;
    }

//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------

    public void setPolygonIndex(int polygonIndex) {
        this.polygonIndex = polygonIndex;
    }
    public void setCurrentPolygonLatLong(ArrayList<LatLng> currentPolygonLatLong) {
        this.currentPolygonLatLong = currentPolygonLatLong;
    }
    public void setPolygonStrokeColor(int polygonStrokeColor) {
        this.polygonStrokeColor = polygonStrokeColor;
    }
    public void setPolygonFillColor(int polygonFillColor) {
        this.polygonFillColor = polygonFillColor;
    }

    public void setProjectLayerModel(ProjectLayerModel projectLayerModel) {
        this.projectLayerModel = projectLayerModel;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public void setPolygonMarker(Marker polygonMarker) {
        this.polygonMarker = polygonMarker;
    }

}
