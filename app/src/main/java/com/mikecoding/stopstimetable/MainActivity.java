package com.mikecoding.stopstimetable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StationInterface {

    //SL Platsuppslag
    private static final String API_KEY = "4cfa136f50c14cb1bad7a91d84ce14f8";
    //SL Realtidsinformation
    //API_KEY = "2bf817b6d911437790124c982f80df7b";

    ListView list;
    EditText inputText;
    String url, location;
    ArrayList<Station> stations;
    ProgressBar progressBar;
    Toast toast;
    TextView text_msg, text_last_search;
    StationAdapter adapter;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        list = (ListView) findViewById(R.id.scroll_list);
        inputText = (EditText) findViewById(R.id.input_text);
        progressBar = (ProgressBar) findViewById(R.id.main_progressbar);
        text_msg = (TextView) findViewById(R.id.main_error_text);
        text_last_search = (TextView) findViewById(R.id.lastsearch);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        getLastResult();
        //här sätts url till ApiCaller(). key=%s = String API_KEY searchstring=%s = String location
        //Api URL:
        // "http://api.sl.se/api2/realtimedeparturesv4.json?key=2bf817b6d911437790124c982f80df7b&siteid=9001&timewindow=5";

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("ID", stations.get(position).getSiteID());
                startActivity(intent);
            }
        });
    }
    public void searchStations(View view){
        if (inputText.getText().toString().trim().isEmpty()) {
            displayToast(R.string.error_textfield_empty);
        }
        else {
            if (adapter != null) {
                adapter.clear();
            }
            location = inputText.getText().toString().trim();
            editor.putString(getString(R.string.last_result), location);
            editor.apply();
            url = createURL(location);
            new ApiCaller(this).execute(url);
            inputText.setText("");
            getLastResult();
        }
    }
    private String createURL (String location) {
        String urlCreation;
        urlCreation = String.format("http://api.sl.se/api2/typeahead.json?key=%s&searchstring=%s&stationsonly=true", API_KEY, location);
        return urlCreation;
    }

    @Override
    public void onTaskComplete(ArrayList<Station> stations){
        if (stations.isEmpty()) {
            if (adapter != null) {
                adapter.clear();
            }
            text_msg.setText(R.string.error_no_stations);
            text_msg.setVisibility(View.VISIBLE);

        } else {
            if (text_msg.getVisibility() == View.VISIBLE) {
                text_msg.setVisibility(View.GONE);
            }
            this.stations = stations;
            adapter = new StationAdapter(this, R.layout.stationlistitem, stations);
            list.setAdapter(adapter);
        }
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void displayToast(int msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
    public void getLastResult() {
        if (sharedPref.contains(getString(R.string.last_result))) {
            text_last_search.setText(sharedPref.getString(getString(R.string.last_result), ""));
        }
    }
    public void doLastSearch(View view) {
        if (!text_last_search.getText().toString().isEmpty()) {
            url = createURL(text_last_search.getText().toString());
            new ApiCaller(this).execute(url);
        }
    }
}
