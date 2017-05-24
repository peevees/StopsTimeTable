package com.mikecoding.stopstimetable;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class ApiCaller extends AsyncTask<String, Void, JSONObject>{

    private JSONHandler jsonHandler;
    private StationInterface stationInterface;
    private InformationInterface informationInterface;

    public ApiCaller(StationInterface context) {
        this.stationInterface = context;
    }
    public ApiCaller(InformationInterface context) {
        this.informationInterface = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (stationInterface != null) {
            stationInterface.showProgressBar();
        }
        if (informationInterface !=null ) {
            informationInterface.showProgressBar();
        }
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String server_response;
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                return new JSONObject(server_response);
            } else {
                urlConnection.disconnect();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        if (stationInterface != null) {
            jsonHandler = new JSONHandler(stationInterface, result);
            jsonHandler.parseStations();
        } else {
            jsonHandler = new JSONHandler(informationInterface, result);
            jsonHandler.parseInformation();
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
