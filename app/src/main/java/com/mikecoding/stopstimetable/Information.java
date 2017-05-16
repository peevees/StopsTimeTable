package com.mikecoding.stopstimetable;

public class Information {

    private String groupOfLine;
    private String displayTime;
    private String transportMode;
    private String lineNumber;
    private String destination;
    private int emptyMessage = 0;

    public Information() {


    }

    public String getGroupOfLine() {
        return groupOfLine;
    }

    public void setGroupOfLine(String groupOfLine) {
        this.groupOfLine = groupOfLine;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(String displayTime) {
        this.displayTime = displayTime;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getEmptyMessage() {
        return emptyMessage;
    }

    public void setEmptyMessage(int emptyMessage) {
        this.emptyMessage = emptyMessage;
    }
}
