package com.mikecoding.stopstimetable;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by micste on 2017-05-10.
 */

public class JSONHandler {

    private JSONObject json;
    private ApiInterface apiInterface;
    private InformationInterface informationInterface;
    private ArrayList<Station> stationList;
    private ArrayList<Information> informationList;
    private Station station;
    private Information information;

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

        }

     }

    public void parseInformation() {

        //Hantering för API 2 Realtidsinformation

        if (json != null) {

            informationList = new ArrayList<>();

            try {

                JSONObject object = json.getJSONObject("ResponseData");
                JSONArray jMetrosArray = object.getJSONArray("Metros");
                JSONArray jBusesArray = object.getJSONArray("Buses");
                JSONArray jTrainsArray = object.getJSONArray("Trains");

                //Handle Metros
                if (jMetrosArray != null) {

                    for (int i = 0; i < jMetrosArray.length(); i++) {
                        information = new Information();
                        information.setGroupOfLine(jMetrosArray.getJSONObject(i).getString("GroupOfLine"));
                        information.setDisplayTime(jMetrosArray.getJSONObject(i).getString("DisplayTime"));
                        information.setLineNumber(jMetrosArray.getJSONObject(i).getString("LineNumber"));
                        information.setTransportMode(jMetrosArray.getJSONObject(i).getString("TransportMode"));
                        information.setDestination(jMetrosArray.getJSONObject(i).getString("Destination"));
                        informationList.add(information);
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
                        informationList.add(information);
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
                        informationList.add(information);
                    }

                }

                informationInterface.hideProgressBar();
                informationInterface.onTaskComplete(informationList);

                //Logg output JSON Datan vi fått in
                for (int i = 0; i < informationList.size(); i++) {
                    Log.d("Realtidsinformation: ", "" + informationList.get(i).getDisplayTime()
                            + ", " + informationList.get(i).getGroupOfLine() + " " + informationList.get(i).getLineNumber()
                            + " mot " + informationList.get(i).getDestination());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            informationInterface.hideProgressBar();
            informationInterface.displayToast("There is no internet connection available");
        }
    }

}




