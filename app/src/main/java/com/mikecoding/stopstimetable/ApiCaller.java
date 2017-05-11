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
    private ApiInterface apiInterface;
    private InformationInterface informationInterface;

    public ApiCaller(ApiInterface context) {
        this.apiInterface = context;
    }
    public ApiCaller(InformationInterface context) {
        this.informationInterface = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (apiInterface != null) {
            apiInterface.showProgressBar();
        }

    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        String server_response;

        URL url;
        HttpURLConnection urlConnection = null;


        Log.d("Test", "doInBackground: ");
        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);


            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.d("Server response", server_response);

                return new JSONObject(server_response);
            } else {

                urlConnection.disconnect();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            Log.d("Test", "doInBackground: sockettimeout");
            e.printStackTrace();

        } catch (IOException e) {
            Log.d("TEST", "doInBackground: ioexception");
            e.printStackTrace();
            return null;
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        super.onPostExecute(result);
        Log.d("test", "onPostExecute: ");

        if (apiInterface != null) {
            jsonHandler = new JSONHandler(apiInterface, result);
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
                    Log.d("TEST", "readStream: ");
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
