package com.surveybaba.DTO;

import java.io.Serializable;

public class ImagSyncVo implements Serializable {

    private String g_id = "";
    private String imagepath = "";

    public ImagSyncVo(String g_id, String imagepath) {
        this.g_id = g_id;
        this.imagepath = imagepath;
    }

    public String getG_id() {
        return g_id;
    }

    public String getImagepath() {
        return imagepath;
    }
}
