package com.surveybaba.model;


public class CameraModule {

    String id;
    String userId;
    String projectId;
    String workId;
    String surveyId;
    String version;
    String imageLat;
    String imageLon;
    String imageDateTime;
    String imageDesc;
    String imagePath;
    public CameraModule(String id, String userId, String version, String imageLat, String imageLon, String imageDateTime, String imageDesc, String imagePath, String name) {
        this.id = id;
        this.userId = userId;
        this.version = version;
        this.imageLat = imageLat;
        this.imageLon = imageLon;
        this.imageDateTime = imageDateTime;
        this.imageDesc = imageDesc;
        this.imagePath = imagePath;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImageLat() {
        return imageLat;
    }

    public void setImageLat(String imageLat) {
        this.imageLat = imageLat;
    }

    public String getImageLon() {
        return imageLon;
    }

    public void setImageLon(String imageLon) {
        this.imageLon = imageLon;
    }

    public String getImageDateTime() {
        return imageDateTime;
    }

    public void setImageDateTime(String imageDateTime) {
        this.imageDateTime = imageDateTime;
    }

    public String getImageDesc() {
        return imageDesc;
    }

    public void setImageDesc(String imageDesc) {
        this.imageDesc = imageDesc;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
