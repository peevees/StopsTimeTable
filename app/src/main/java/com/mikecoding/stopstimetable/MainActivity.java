package com.mikecoding.stopstimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "4cfa136f50c14cb1bad7a91d84ce14f8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String location = "södra";
        String url = String.format("http://api.sl.se/api2/typeahead.json?key=%s&searchstring=%s&stationsonly=true", API_KEY, location);

        new ApiCaller().execute(url);
    }
}
