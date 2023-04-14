package com.surveybaba.model;

public class GpsTrackingModule {

    String id;
    String user_id;
    String type;
    String latLong;
    String datetime;
    String version;
    String unique_number;
    String token;

    public GpsTrackingModule(String id, String user_id, String type, String latLong, String datetime, String version, String token,String unique_number) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.latLong = latLong;
        this.datetime = datetime;
        this.version = version;
        this.token = token;
        this.unique_number = unique_number;
    }

    public String getUnique_number() {
        return unique_number;
    }

    public void setUnique_number(String unique_number) {
        this.unique_number = unique_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
