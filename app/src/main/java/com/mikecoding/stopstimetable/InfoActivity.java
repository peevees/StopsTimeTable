package com.mikecoding.stopstimetable;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.ArrayList;

public class InfoActivity extends AppCompatActivity implements InformationInterface{

    private static final String API_KEY = "2bf817b6d911437790124c982f80df7b";
    ListView list_subway, list_trains, list_buses;
    ArrayAdapter subwayAdapter, trainAdapter, busAdapter;
    String siteId;
    String url;
    ProgressBar progressBar;
    TextView textView_msg, timeText, lastUpdateText;
    ImageButton buttonSubway, buttonTrain, buttonBus;
    String time;
    int progressNumber;
    int btnColorActive, btnColorDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        btnColorActive = ContextCompat.getColor(this, R.color.colorLightBlue);
        btnColorDefault = ContextCompat.getColor(this, R.color.colorPrimary);
        progressBar = (ProgressBar) findViewById(R.id.info_progressbar);
        textView_msg = (TextView) findViewById(R.id.info_error_text);
        lastUpdateText = (TextView) findViewById(R.id.last_update);
        buttonSubway = (ImageButton) findViewById(R.id.btn_subway);
        buttonTrain = (ImageButton) findViewById(R.id.btn_train);
        buttonBus = (ImageButton) findViewById(R.id.btn_bus);
        siteId = getIntent().getExtras().getString("ID");
        time = "5";
        progressNumber = Integer.parseInt(time)-5;
        apiCalling();

        list_subway = (ListView) findViewById(R.id.listview_subway);
        list_trains = (ListView) findViewById(R.id.listview_trains);
        list_buses = (ListView) findViewById(R.id.listview_buses);

    }
    public void apiCalling(){

        url = String.format("http://api.sl.se/api2/realtimedeparturesv4.json?key=%s&siteid=%s&timewindow=%s", API_KEY, siteId, time);
        new ApiCaller(this).execute(url);
    }
    @Override
    public void onTaskComplete(ArrayList<Information> subwayInfo, ArrayList<Information> trainInfo,
                               ArrayList<Information> busInfo) {

        subwayAdapter = new InformationAdapter(this, R.layout.informationlistitem, subwayInfo);
        list_subway.setAdapter(subwayAdapter);

        trainAdapter = new InformationAdapter(this, R.layout.informationlistitem, trainInfo);
        list_trains.setAdapter(trainAdapter);

        busAdapter = new InformationAdapter(this, R.layout.informationlistitem, busInfo);
        list_buses.setAdapter(busAdapter);
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
    public void lastUpdate(String time) {
        lastUpdateText.setText(getResources().getString(R.string.last_update) + time);
    }
    @Override
    public void displayToast(int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_time:
                //user choose time show time dialog
                showDialog();
                return true;
            case R.id.action_refresh:
                apiCalling();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDialog(){
        View view = View.inflate(this, R.layout.time_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do something with input
                time = timeText.getText().toString();
                apiCalling();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
            }
        });
        builder.show();
        timeText = (TextView) view.findViewById(R.id.time_text);
        final SeekBar timeInput = (SeekBar) view.findViewById(R.id.time_input);
        timeInput.setProgress(progressNumber);
        timeInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //sets the TextView that displays time to progressbar input with
                // an increment of 5 for each step
                timeText.setText(String.valueOf(5 + (progress * 5)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void toggleView(View view) {

        switch (view.getId()) {
            case R.id.btn_subway:
                list_trains.setVisibility(View.GONE);
                list_buses.setVisibility(View.GONE);
                list_subway.setVisibility(View.VISIBLE);
                buttonSubway.setBackgroundColor(btnColorActive);
                buttonTrain.setBackgroundColor(btnColorDefault);
                buttonBus.setBackgroundColor(btnColorDefault);
                break;
            case R.id.btn_train:
                list_subway.setVisibility(View.GONE);
                list_buses.setVisibility(View.GONE);
                list_trains.setVisibility(View.VISIBLE);
                buttonTrain.setBackgroundColor(btnColorActive);
                buttonSubway.setBackgroundColor(btnColorDefault);
                buttonBus.setBackgroundColor(btnColorDefault);
                break;
            case R.id.btn_bus:
                list_trains.setVisibility(View.GONE);
                list_subway.setVisibility(View.GONE);
                list_buses.setVisibility(View.VISIBLE);
                buttonBus.setBackgroundColor(btnColorActive);
                buttonSubway.setBackgroundColor(btnColorDefault);
                buttonTrain.setBackgroundColor(btnColorDefault);
                break;
        }

    }

}