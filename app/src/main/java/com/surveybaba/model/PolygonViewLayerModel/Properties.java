package com.surveybaba.model.PolygonViewLayerModel;

import com.google.gson.annotations.SerializedName;

public class Properties {

    private String objectid;
    private String id;
    private String route_id;
    private String app_user_id;
    private String app_user_name;
    private String marker_type;
    private String comments;
    private String created_at;
    private String created_user;
    private String created_date;
    private String last_edited_user;
    private String last_edited_date;
    private String reference_id;
    private String area_sqmtrs;
    private String editable;
    private String globalid;
    private String sync_status;
    @SerializedName("Shape__Area")
    private String shape_Area;
    @SerializedName("Shape__Length")
    private String shape_Length;

//------------------------------------------------------- Getter ------------------------------------------------------------------------------------------------------------------------------------------------

    public String getObjectid() {
        return objectid;
    }

    public String getId() {
        return id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public String getApp_user_name() {
        return app_user_name;
    }

    public String getMarker_type() {
        return marker_type;
    }

    public String getComments() {
        return comments;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCreated_user() {
        return created_user;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getLast_edited_user() {
        return last_edited_user;
    }

    public String getLast_edited_date() {
        return last_edited_date;
    }

    public String getReference_id() {
        return reference_id;
    }

    public String getArea_sqmtrs() {
        return area_sqmtrs;
    }

    public String getEditable() {
        return editable;
    }

    public String getGlobalid() {
        return globalid;
    }

    public String getSync_status() {
        return sync_status;
    }

    public String getShape_Area() {
        return shape_Area;
    }

    public String getShape_Length() {
        return shape_Length;
    }


//------------------------------------------------------- Setter ------------------------------------------------------------------------------------------------------------------------------------------------


    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public void setApp_user_name(String app_user_name) {
        this.app_user_name = app_user_name;
    }

    public void setMarker_type(String marker_type) {
        this.marker_type = marker_type;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public void setLast_edited_user(String last_edited_user) {
        this.last_edited_user = last_edited_user;
    }

    public void setLast_edited_date(String last_edited_date) {
        this.last_edited_date = last_edited_date;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public void setArea_sqmtrs(String area_sqmtrs) {
        this.area_sqmtrs = area_sqmtrs;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public void setGlobalid(String globalid) {
        this.globalid = globalid;
    }

    public void setSync_status(String sync_status) {
        this.sync_status = sync_status;
    }

    public void setShape_Area(String shape_Area) {
        this.shape_Area = shape_Area;
    }

    public void setShape_Length(String shape_Length) {
        this.shape_Length = shape_Length;
    }
}
