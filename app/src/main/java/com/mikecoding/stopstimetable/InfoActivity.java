package com.mikecoding.stopstimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity implements InformationInterface{

    private static final String API_KEY = "2bf817b6d911437790124c982f80df7b";
    ListView lv_info;
    ArrayAdapter mAdapter;
    String siteId;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //TODO Fixa så att användaren kan ange tidsram för sökning
        //TODO fixa informationslistitem xml lite
        siteId = getIntent().getExtras().getString("ID");
        String url = String.format("http://api.sl.se/api2/realtimedeparturesv4.json?key=%s&siteid=%s&timewindow=5", API_KEY, siteId);
        new ApiCaller(this, API_KEY).execute(url);

        lv_info = (ListView) findViewById(R.id.listview_information);

    }
    @Override
    public void onTaskComplete(ArrayList<Information> informations) {

        mAdapter = new InformationAdapter(this, R.layout.informationlistitem, informations);
        lv_info.setAdapter(mAdapter);
    }
}
