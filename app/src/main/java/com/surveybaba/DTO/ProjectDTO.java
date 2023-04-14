package com.surveybaba.DTO;

import java.io.Serializable;

public class ProjectDTO implements Serializable {

    private String id = "";
    private String project = "";
    private String latitude = "";
    private String longitude = "";

    public ProjectDTO(String id, String project, String latitude, String longitude) {
        this.id = id;
        this.project = project;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getProject() {
        return project;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
