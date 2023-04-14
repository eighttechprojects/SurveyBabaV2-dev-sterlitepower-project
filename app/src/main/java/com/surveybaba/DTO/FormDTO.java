package com.surveybaba.DTO;

import java.io.Serializable;

public class FormDTO implements Serializable {

    private String form_id = "";
    private String description = "";
    private String project_id = "";

    public FormDTO(String form_id, String description, String project_id) {
        this.form_id = form_id;
        this.description = description;
        this.project_id = project_id;
    }

    public String getForm_id() {
        return form_id;
    }

    public String getDescription() {
        return description;
    }

    public String getProject_id() {
        return project_id;
    }
}
