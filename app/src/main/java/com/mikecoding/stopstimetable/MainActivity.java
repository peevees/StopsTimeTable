package com.mikecoding.stopstimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //SL Platsuppslag
    private static final String API_KEY = "4cfa136f50c14cb1bad7a91d84ce14f8";
    //SL Realtidsinformation
    private static final String API_KEY2 = "2bf817b6d911437790124c982f80df7b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String location = "södra";
        //här sätts url till ApiCaller(). key=%s = String API_KEY searchstring=%s = String location
        String url = String.format("http://api.sl.se/api2/typeahead.json?key=%s&searchstring=%s&stationsonly=true", API_KEY, location);
        String urlTest = "http://api.sl.se/api2/realtimedeparturesv4.json?key=2bf817b6d911437790124c982f80df7b&siteid=9192&timewindow=5";

        new ApiCaller(API_KEY2).execute(urlTest);
    }
}
