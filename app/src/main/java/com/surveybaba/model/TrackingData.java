package com.surveybaba.model;

public class TrackingData {
    String user_id;
    String latitude;
    String longitude;
    String isInsideCoverageArea;

    public TrackingData(String user_id, String latitude, String longitude, String isInsideCoverageArea) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isInsideCoverageArea = isInsideCoverageArea;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsInsideCoverageArea() {
        return isInsideCoverageArea;
    }

    public void setIsInsideCoverageArea(String isInsideCoverageArea) {
        this.isInsideCoverageArea = isInsideCoverageArea;
    }
}
