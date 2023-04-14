package com.surveybaba.model;

import com.google.gson.annotations.SerializedName;
import com.surveybaba.FormBuilder.FormDetailData;
import java.util.ArrayList;

public class SurveyFormModel {

    @SerializedName("form")
    private ArrayList<FormDetailData> form;

    @SerializedName("form_latlong")
    private ArrayList<FormLatLon> form_latlong;


    private String formbg_color;
    private String form_logo;
    private String form_sno;

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

    public ArrayList<FormDetailData> getForm() {
        return form;
    }

    public void setForm(ArrayList<FormDetailData> form) {
        this.form = form;
    }

    public ArrayList<FormLatLon> getForm_latlong() {
        return form_latlong;
    }

    public void setForm_latlong(ArrayList<FormLatLon> form_latlong) {
        this.form_latlong = form_latlong;
    }

}
