package com.surveybaba.setting.model;

public class BinRecordProfile {
    String name, category;
    int vertexRecordingIndex, noOfTracksToDisplay;
    double distanceInterval, timeInterval, gpsAccuracy;
    boolean isHeader, isRecordOnlyWhenMoving, isRecordWhenGpsOff;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistanceInterval() {
        return distanceInterval;
    }

    public void setDistanceInterval(double distanceInterval) {
        this.distanceInterval = distanceInterval;
    }

    public double getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(double timeInterval) {
        this.timeInterval = timeInterval;
    }

    public double getGpsAccuracy() {
        return gpsAccuracy;
    }

    public void setGpsAccuracy(double gpsAccuracy) {
        this.gpsAccuracy = gpsAccuracy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getVertexRecordingIndex() {
        return vertexRecordingIndex;
    }

    public void setVertexRecordingIndex(int vertexRecordingIndex) {
        this.vertexRecordingIndex = vertexRecordingIndex;
    }

    public int getNoOfTracksToDisplay() {
        return noOfTracksToDisplay;
    }

    public void setNoOfTracksToDisplay(int noOfTracksToDisplay) {
        this.noOfTracksToDisplay = noOfTracksToDisplay;
    }

    public boolean isRecordOnlyWhenMoving() {
        return isRecordOnlyWhenMoving;
    }

    public void setRecordOnlyWhenMoving(boolean recordOnlyWhenMoving) {
        isRecordOnlyWhenMoving = recordOnlyWhenMoving;
    }

    public boolean isRecordWhenGpsOff() {
        return isRecordWhenGpsOff;
    }

    public void setRecordWhenGpsOff(boolean recordWhenGpsOff) {
        isRecordWhenGpsOff = recordWhenGpsOff;
    }
}
