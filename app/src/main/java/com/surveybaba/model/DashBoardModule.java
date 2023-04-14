package com.surveybaba.model;

public class DashBoardModule {

    private String module_name;
    private String module;

    public DashBoardModule(String module_name, String module) {
        this.module_name = module_name;
        this.module = module;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
