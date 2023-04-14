package com.surveybaba.model;


public class TimeLineModel  {

    private String imagePath;
    private String description;
    private String datetime;



    public String getImagePath() {
        return imagePath;
    }
    public String getDatetime() {
        return datetime;
    }
    public String getDescription() {
        return description;
    }


    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setDescription(String description) {
        this.description = description;
    }



}
