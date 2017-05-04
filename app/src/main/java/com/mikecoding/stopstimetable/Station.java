package com.mikecoding.stopstimetable;

public class Station {

    private String name;
    private String siteID;

    public Station() {

    }

    public Station(String name, String siteID) {

        this.name = name;
        this.siteID = siteID;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getName() {
        return name;
    }

    public String getSiteID() {
        return siteID;
    }
}
