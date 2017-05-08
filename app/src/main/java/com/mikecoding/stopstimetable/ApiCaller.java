package com.mikecoding.stopstimetable;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ApiCaller extends AsyncTask<String, Void, JSONObject>{

    private ArrayList<Station> stationList;
    private ArrayList<Information> informationList;
    private Station station;
    private Information information;
    private String key;
    private ApiInterface apiInterface;

    public ApiCaller(ApiInterface context, String key) {
        this.key = key;
        this.apiInterface = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //progressdialog kod här
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String server_response;

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.d("Server response", server_response);

                return new JSONObject(server_response);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);

        //Hantering för API Hållplatser
        //Skapar station objekt av JSON datan och sätter in dessa i en ArrayList
        if (key.equals("4cfa136f50c14cb1bad7a91d84ce14f8")) {

            if (result != null) {

                stationList = new ArrayList<>();

                try {
                    JSONArray jArray = result.getJSONArray("ResponseData");

                    for (int i = 0; i < jArray.length(); i++) {

                        station = new Station();
                        station.setName(jArray.getJSONObject(i).getString("Name"));
                        station.setSiteID(jArray.getJSONObject(i).getString("SiteId"));
                        stationList.add(station);

                        Log.d("Station", "Name: " + station.getName() + " ID: " + station.getSiteID());
                    }
                    apiInterface.onTaskComplete(stationList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        //Hantering för API 2 Realtidsinformation
        else {

            if (result != null) {

                informationList = new ArrayList<>();

                try {

                    JSONObject object = result.getJSONObject("ResponseData");
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
                            information.setGroupOfLine(jBusesArray.getJSONObject(i).getString("GroupOfLine"));
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
                            information.setGroupOfLine(jTrainsArray.getJSONObject(i).getString("GroupOfLine"));
                            information.setDisplayTime(jTrainsArray.getJSONObject(i).getString("DisplayTime"));
                            information.setLineNumber(jTrainsArray.getJSONObject(i).getString("LineNumber"));
                            information.setTransportMode(jTrainsArray.getJSONObject(i).getString("TransportMode"));
                            information.setDestination(jTrainsArray.getJSONObject(i).getString("Destination"));
                            informationList.add(information);
                        }

                    }



                    //Logg output JSON Datan vi fått in
                    for (int i = 0; i < informationList.size(); i++) {
                        Log.d("Realtidsinformation: ", "" + informationList.get(i).getDisplayTime()
                        + ", " + informationList.get(i).getGroupOfLine() + " " + informationList.get(i).getLineNumber()
                        + " mot " + informationList.get(i).getDestination());
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
