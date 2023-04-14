package com.surveybaba.model;

import com.surveybaba.R;

public class UserSearchModel {

    private String photo;
    private String name;
    private String emailId;
    private String phone;
    private String designation;
    private String description;
    private String typeOfSurveyors;
    private String typeOfContractor;


    public UserSearchModel(String photo, String name, String emailId, String phone, String designation, String description) {
        this.photo = photo;
        this.name = name;
        this.emailId = emailId;
        this.phone = phone;
        this.designation = designation;
        this.description = description;
    }

    public String getTypeOfSurveyors() {
        return typeOfSurveyors;
    }

    public void setTypeOfSurveyors(String typeOfSurveyors) {
        this.typeOfSurveyors = typeOfSurveyors;
    }

    public String getTypeOfContractor() {
        return typeOfContractor;
    }

    public void setTypeOfContractor(String typeOfContractor) {
        this.typeOfContractor = typeOfContractor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
