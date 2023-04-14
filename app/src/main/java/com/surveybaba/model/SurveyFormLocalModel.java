package com.surveybaba.model;

import com.google.gson.annotations.SerializedName;
import com.surveybaba.FormBuilder.FormDetailData;
import com.surveybaba.FormBuilder.OtherFormData;

import java.util.ArrayList;

public class SurveyFormLocalModel {

    @SerializedName("form_data")
    public ArrayList<FormDetailData> form_data;
    public String formID;
    public String layerID;
    @SerializedName("other_data")
    public OtherFormData other_data;
    public String projectID;
    public String workID;

    private String type;
    private String icon_width;
    private String icon_height;
    private String line_color;
    private String line_type;

    private String formbg_color;
    private String form_logo;
    private String form_sno;

    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public String getFormbg_color() {
        return formbg_color;
    }

    public void setFormbg_color(String formbg_color) {
        this.formbg_color = formbg_color;
    }

    public String getForm_logo() {
        return form_logo;
    }

    public void setForm_logo(String form_logo) {
        this.form_logo = form_logo;
    }

    public String getForm_sno() {
        return form_sno;
    }

    public void setForm_sno(String form_sno) {
        this.form_sno = form_sno;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon_width() {
        return icon_width;
    }

    public void setIcon_width(String icon_width) {
        this.icon_width = icon_width;
    }

    public String getIcon_height() {
        return icon_height;
    }

    public void setIcon_height(String icon_height) {
        this.icon_height = icon_height;
    }

    public String getLine_color() {
        return line_color;
    }

    public void setLine_color(String line_color) {
        this.line_color = line_color;
    }

    public ArrayList<FormDetailData> getForm_data() {
        return form_data;
    }

    public void setForm_data(ArrayList<FormDetailData> form_data) {
        this.form_data = form_data;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getLayerID() {
        return layerID;
    }

    public void setLayerID(String layerID) {
        this.layerID = layerID;
    }

    public OtherFormData getOther_data() {
        return other_data;
    }

    public void setOther_data(OtherFormData other_data) {
        this.other_data = other_data;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }
}
