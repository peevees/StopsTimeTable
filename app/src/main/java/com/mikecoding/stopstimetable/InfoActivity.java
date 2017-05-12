package com.mikecoding.stopstimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity implements InformationInterface{

    private static final String API_KEY = "2bf817b6d911437790124c982f80df7b";
    ListView lv_info;
    ArrayAdapter mAdapter;
    String siteId;
    String url;
    ProgressBar progressBar;
    TextView textView_msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //TODO Fixa så att användaren kan ange tidsram för sökning
        //TODO fixa informationslistitem xml lite
        //TODO Fixa så att ett meddelande visas när man ej får fram någon realtidsinformation, om den ej innehåller någon data
        progressBar = (ProgressBar) findViewById(R.id.info_progressbar);
        textView_msg = (TextView) findViewById(R.id.info_error_text);
        siteId = getIntent().getExtras().getString("ID");
        String url = String.format("http://api.sl.se/api2/realtimedeparturesv4.json?key=%s&siteid=%s&timewindow=1", API_KEY, siteId);
        new ApiCaller(this).execute(url);

        lv_info = (ListView) findViewById(R.id.listview_information);

    }
    @Override
    public void onTaskComplete(ArrayList<Information> informations) {

        if (informations.isEmpty()) {
            textView_msg.setText(R.string.error_no_departures);
            textView_msg.setVisibility(View.VISIBLE);
        } else {
            mAdapter = new InformationAdapter(this, R.layout.informationlistitem, informations);
            lv_info.setAdapter(mAdapter);
        }
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    @Override
    public void displayToast(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
