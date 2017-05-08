package com.mikecoding.stopstimetable;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ApiInterface {

    //SL Platsuppslag
    private static final String API_KEY = "4cfa136f50c14cb1bad7a91d84ce14f8";
    //SL Realtidsinformation
    private static final String API_KEY2 = "2bf817b6d911437790124c982f80df7b";

    ListView list;
    EditText inputText;
    String url;
    ArrayList<Station> stations;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.scroll_list);
        inputText = (EditText) findViewById(R.id.input_text);

        String location = "södra";
        //här sätts url till ApiCaller(). key=%s = String API_KEY searchstring=%s = String location

        String urlTest = "http://api.sl.se/api2/realtimedeparturesv4.json?key=2bf817b6d911437790124c982f80df7b&siteid=9001&timewindow=5";
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "HELLO FROM ITEM CLICKED " + position);
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("ID", stations.get(position).getSiteID());
                startActivity(intent);
            }
        });
    }
    public void searchStations(View view){
       url = createURL(inputText.getText().toString());
        new ApiCaller(this, API_KEY).execute(url);
    }
    private String createURL (String location){
        String urlCreation;
        urlCreation = String.format("http://api.sl.se/api2/typeahead.json?key=%s&searchstring=%s&stationsonly=true", API_KEY, location);
        return urlCreation;
    }

    @Override
    public void onTaskComplete(ArrayList<Station> stations){
        this.stations = stations;
        StationAdapter adapter = new StationAdapter(this, R.layout.stationlistitem, stations);
        list.setAdapter(adapter);
    }
}
