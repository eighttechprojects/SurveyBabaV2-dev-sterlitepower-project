package com.surveybaba.model.LayerMoveModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.surveybaba.model.ProjectLayerModel;

public class PointMoveModel {

    private int pointIndex;
    private LatLng currentPointLatLong;
    private String pointIcon;
    private String pointWidth;
    private String pointHeight;
    private ProjectLayerModel projectLayerModel;
    private Marker pointMarker;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public int getPointIndex() {
        return pointIndex;
    }

    public LatLng getCurrentPointLatLong() {
        return currentPointLatLong;
    }

    public String getPointIcon() {
        return pointIcon;
    }

    public ProjectLayerModel getProjectLayerModel() {
        return projectLayerModel;
    }

    public Marker getPointMarker() {
        return pointMarker;
    }

    public String getPointWidth() {
        return pointWidth;
    }

    public String getPointHeight() {
        return pointHeight;
    }

    //------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setPointIndex(int pointIndex) {
        this.pointIndex = pointIndex;
    }

    public void setCurrentPointLatLong(LatLng currentPointLatLong) {
        this.currentPointLatLong = currentPointLatLong;
    }

    public void setPointIcon(String pointIcon) {
        this.pointIcon = pointIcon;
    }

    public void setProjectLayerModel(ProjectLayerModel projectLayerModel) {
        this.projectLayerModel = projectLayerModel;
    }

    public void setPointMarker(Marker pointMarker) {
        this.pointMarker = pointMarker;
    }

    public void setPointWidth(String pointWidth) {
        this.pointWidth = pointWidth;
    }

    public void setPointHeight(String pointHeight) {
        this.pointHeight = pointHeight;
    }
}
