package com.mikecoding.stopstimetable;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class JSONHandler {

    private JSONObject json;
    private ApiInterface apiInterface;
    private InformationInterface informationInterface;
    private ArrayList<Station> stationList;
    private ArrayList<Information> subwayInfo, busInfo, trainInfo;
    private Station station;
    private Information information;
    private String lastUpdate;

    public JSONHandler(ApiInterface context, JSONObject json) {
        this.json = json;
        this.apiInterface = context;
    }
    public JSONHandler(InformationInterface context, JSONObject json) {
        this.json = json;
        this.informationInterface = context;
    }

    public void parseStations() {

        //Hantering för API Hållplatser
        //Skapar station objekt av JSON datan och sätter in dessa i en ArrayList
        if (json != null) {

            stationList = new ArrayList<>();

            try {
                JSONArray jArray = json.getJSONArray("ResponseData");

                for (int i = 0; i < jArray.length(); i++) {

                    station = new Station();
                    station.setName(jArray.getJSONObject(i).getString("Name"));
                    station.setSiteID(jArray.getJSONObject(i).getString("SiteId"));
                    stationList.add(station);

                    Log.d("Station", "Name: " + station.getName() + " ID: " + station.getSiteID());
                }
                apiInterface.hideProgressBar();
                apiInterface.onTaskComplete(stationList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            apiInterface.hideProgressBar();
            apiInterface.displayToast(R.string.error_no_internet);
        }

     }

    public void parseInformation() {

        //Hantering för API 2 Realtidsinformation

        if (json != null) {

            subwayInfo = new ArrayList<>();
            trainInfo = new ArrayList<>();
            busInfo = new ArrayList<>();

            try {

                JSONObject object = json.getJSONObject("ResponseData");
                JSONArray jMetrosArray = object.getJSONArray("Metros");
                JSONArray jBusesArray = object.getJSONArray("Buses");
                JSONArray jTrainsArray = object.getJSONArray("Trains");

                String updateTime = object.getString("LatestUpdate");
                lastUpdate = updateTime.substring(updateTime.indexOf("T") + 1);

                //Handle Metros
                if (jMetrosArray != null) {

                    for (int i = 0; i < jMetrosArray.length(); i++) {
                        information = new Information();
                        information.setGroupOfLine(jMetrosArray.getJSONObject(i).getString("GroupOfLine"));
                        information.setDisplayTime(jMetrosArray.getJSONObject(i).getString("DisplayTime"));
                        information.setLineNumber(jMetrosArray.getJSONObject(i).getString("LineNumber"));
                        information.setTransportMode(jMetrosArray.getJSONObject(i).getString("TransportMode"));
                        information.setDestination(jMetrosArray.getJSONObject(i).getString("Destination"));
                        subwayInfo.add(information);
                    }

                }

                //Handle Buses
                if (jBusesArray != null) {

                    for (int i= 0; i < jBusesArray.length(); i++) {
                        information = new Information();
                        if (jBusesArray.getJSONObject(i).getString("GroupOfLine").equals("blåbuss")) {
                            information.setGroupOfLine(jBusesArray.getJSONObject(i).getString("GroupOfLine"));
                        } else {
                            information.setGroupOfLine("buss");
                        }
                        information.setDisplayTime(jBusesArray.getJSONObject(i).getString("DisplayTime"));
                        information.setLineNumber(jBusesArray.getJSONObject(i).getString("LineNumber"));
                        information.setTransportMode(jBusesArray.getJSONObject(i).getString("TransportMode"));
                        information.setDestination(jBusesArray.getJSONObject(i).getString("Destination"));
                        busInfo.add(information);
                    }

                }

                //Handle Trains

                if (jTrainsArray != null) {

                    for (int i = 0; i < jTrainsArray.length(); i++) {
                        information = new Information();
                        information.setGroupOfLine("pendeltåg");
                        information.setDisplayTime(jTrainsArray.getJSONObject(i).getString("DisplayTime"));
                        information.setLineNumber(jTrainsArray.getJSONObject(i).getString("LineNumber"));
                        information.setTransportMode(jTrainsArray.getJSONObject(i).getString("TransportMode"));
                        information.setDestination(jTrainsArray.getJSONObject(i).getString("Destination"));
                        trainInfo.add(information);
                    }

                }

                if (subwayInfo.isEmpty()) {
                    information = new Information();
                    information.setEmptyMessage(R.string.error_no_departures);
                    subwayInfo.add(information);
                }
                if (trainInfo.isEmpty()) {
                    information = new Information();
                    information.setEmptyMessage(R.string.error_no_departures);
                    trainInfo.add(information);
                }
                if (busInfo.isEmpty()) {
                    information = new Information();
                    information.setEmptyMessage(R.string.error_no_departures);
                    busInfo.add(information);
                }
                //Sort arraylist by variable displayTime in desc order

                informationInterface.hideProgressBar();
                informationInterface.onTaskComplete(subwayInfo, trainInfo, busInfo);
                informationInterface.lastUpdate(lastUpdate);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            informationInterface.hideProgressBar();
            informationInterface.displayToast(R.string.error_no_internet);
        }
    }

}




