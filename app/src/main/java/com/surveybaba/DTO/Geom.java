package com.surveybaba.DTO;

import java.io.Serializable;

public class Geom implements Serializable {

    private String gid = "";
	private String project_id = "";
	private String form_id = "";
	private String geom_type = "";
	private String geom_array = "";
	private String latitude = "";
	private String longitude = "";
	private String accuracy = "";
	private String record_date = "";
	private String viewData = "";
	private String syncData = "";
	private String IS_SYNC = "";

    public Geom(String gid, String project_id, String form_id, String geom_type, String geom_array, String latitude, String longitude, String accuracy, String record_date, String viewData, String syncData, String IS_SYNC) {
        this.gid = gid;
        this.project_id = project_id;
        this.form_id = form_id;
        this.geom_type = geom_type;
        this.geom_array = geom_array;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.record_date = record_date;
        this.viewData = viewData;
        this.syncData = syncData;
        this.IS_SYNC = IS_SYNC;
    }

    public String getGid() {
        return gid;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public String getGeom_type() {
        return geom_type;
    }

    public String getGeom_array() {
        return geom_array;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public String getRecord_date() {
        return record_date;
    }

    public String getViewData() {
        return viewData;
    }

    public String getSyncData() {
        return syncData;
    }

    public String getIS_SYNC() {
        return IS_SYNC;
    }
}
