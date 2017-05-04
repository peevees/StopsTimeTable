package com.mikecoding.stopstimetable;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ApiCaller extends AsyncTask<String, Void, JSONObject> {

    private ProgressBar progressBar;
    private ArrayList<Station> stationList;
    private Station station;
    private Information information;
    private String key;

    /*
    public GetAdressTask(ProgressBar progressBar) {

        this.progressBar = progressBar;

    }
    */

    public ApiCaller(String key) {
        this.key = key;
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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
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


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        //Hantering för API 2 Realtidsinformation
        else {

            if (result != null) {

                try {

                    JSONObject object = result.getJSONObject("ResponseData");
                    JSONArray jArray2 = object.getJSONArray("Metros");

                    information = new Information();

                    information.setGroupOfLine(jArray2.getJSONObject(0).getString("GroupOfLine"));

                    Log.d("Test", "onPostExecute: " + information.getGroupOfLine());


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
