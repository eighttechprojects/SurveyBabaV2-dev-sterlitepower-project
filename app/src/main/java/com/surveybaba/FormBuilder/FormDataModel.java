package com.surveybaba.FormBuilder;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FormDataModel {

    private String ID;
    private String workID;
    private String formID;
    private String layerID;
    private String projectID;
    private String surveyID;
    private String userID;
    private String formData;
    private String uniqueNumber;
    private String fileContain;
    private String cameraContain;
    private String videoContain;
    private String audioContain;
    private String fileColNumber;
    private String cameraColNumber;
    private String videoColNumber;
    private String audioColNumber;

    private String type;
    private String icon;
    private String icon_width;
    private String icon_height;
    private String line_color;
    private String line_type;

    private String form_sno;
    private String formbg_color;
    private String form_logo;

    @SerializedName("other_data")
    private OtherFormData otherFormData;

    @SerializedName("form_data")
    private ArrayList<FormDetailData> formDetailData;



    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public String getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(String surveyID) {
        this.surveyID = surveyID;
    }

    public String getForm_sno() {
        return form_sno;
    }

    public void setForm_sno(String form_sno) {
        this.form_sno = form_sno;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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


    public ArrayList<FormDetailData> getFormDetailData() {
        return formDetailData;
    }

    public void setFormDetailData(ArrayList<FormDetailData> formDetailData) {
        this.formDetailData = formDetailData;
    }

    public String getLayerID() {
        return layerID;
    }

    public void setLayerID(String layerID) {
        this.layerID = layerID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public OtherFormData getOtherFormData() {
        return otherFormData;
    }

    public void setOtherFormData(OtherFormData otherFormData) {
        this.otherFormData = otherFormData;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getFileContain() {
        return fileContain;
    }

    public void setFileContain(String fileContain) {
        this.fileContain = fileContain;
    }

    public String getCameraContain() {
        return cameraContain;
    }

    public void setCameraContain(String cameraContain) {
        this.cameraContain = cameraContain;
    }

    public String getVideoContain() {
        return videoContain;
    }

    public void setVideoContain(String videoContain) {
        this.videoContain = videoContain;
    }

    public String getAudioContain() {
        return audioContain;
    }

    public void setAudioContain(String audioContain) {
        this.audioContain = audioContain;
    }

    public String getCameraColNumber() {
        return cameraColNumber;
    }

    public void setCameraColNumber(String cameraColNumber) {
        this.cameraColNumber = cameraColNumber;
    }

    public String getVideoColNumber() {
        return videoColNumber;
    }

    public void setVideoColNumber(String videoColNumber) {
        this.videoColNumber = videoColNumber;
    }

    public String getAudioColNumber() {
        return audioColNumber;
    }

    public void setAudioColNumber(String audioColNumber) {
        this.audioColNumber = audioColNumber;
    }

    public String getFileColNumber() {
        return fileColNumber;
    }

    public void setFileColNumber(String fileColNumber) {
        this.fileColNumber = fileColNumber;
    }


    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWorkID() {
        return workID;
    }

    public void setWorkID(String workID) {
        this.workID = workID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }
}
