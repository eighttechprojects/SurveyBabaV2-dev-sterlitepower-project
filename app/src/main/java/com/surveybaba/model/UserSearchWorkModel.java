package com.surveybaba.model;

public class UserSearchWorkModel {

    private String photo;
    private String name;
    private String emailId;
    private String phone;
    private String designation;
    private String description;
    private String state;
    private String city;
    private String areaOfWork;
    private String expertise;
    private String location;
    private String postedBy;
    private String date;

    public UserSearchWorkModel(String photo, String name, String emailId, String phone, String designation, String description) {
        this.photo = photo;
        this.name = name;
        this.emailId = emailId;
        this.phone = phone;
        this.designation = designation;
        this.description = description;
    }

    public UserSearchWorkModel(String photo, String name, String emailId, String phone, String designation, String description, String state, String city, String areaOfWork, String expertise, String location) {
        this.photo = photo;
        this.name = name;
        this.emailId = emailId;
        this.phone = phone;
        this.designation = designation;
        this.description = description;
        this.state = state;
        this.city = city;
        this.areaOfWork = areaOfWork;
        this.expertise = expertise;
        this.location = location;
    }

    public UserSearchWorkModel(String name, String location ,String description, String postedBy, String date) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.postedBy = postedBy;
        this.date = date;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaOfWork() {
        return areaOfWork;
    }

    public void setAreaOfWork(String areaOfWork) {
        this.areaOfWork = areaOfWork;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
