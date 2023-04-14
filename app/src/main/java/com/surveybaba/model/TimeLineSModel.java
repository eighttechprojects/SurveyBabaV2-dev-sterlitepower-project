package com.surveybaba.model;


public class TimeLineSModel  {

    private String imagePath;
    private String description;
    private String datetime;

    public String getImagePath() {
        return imagePath;
    }

    public String getDatetime() {
        return datetime;
    }


    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
