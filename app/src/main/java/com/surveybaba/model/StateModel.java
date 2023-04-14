package com.surveybaba.model;

public class StateModel {

    private String stateID;
    private String stateName;

    public StateModel(String stateID, String stateName) {
        this.stateID = stateID;
        this.stateName = stateName;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
