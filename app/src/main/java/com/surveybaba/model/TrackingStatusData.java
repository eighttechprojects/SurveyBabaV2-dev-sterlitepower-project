package com.surveybaba.model;

public class TrackingStatusData {

    String id;
    String user_id;
    String latitude;
    String longitude;
    String created_date;
    String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TrackingStatusData(String id, String user_id, String latitude, String longitude, String created_date, String version) {
        this.id = id;
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_date = created_date;
        this.version = version;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
