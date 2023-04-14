package com.surveybaba.DTO;

import java.io.Serializable;

public class FromDetailsVO implements Serializable {

    private String lable = "";
    private String value = "";
    private String type = "";

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
