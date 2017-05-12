package com.mikecoding.stopstimetable;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.SeekBar;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //TODO Fixa så att användaren kan ange tidsram för sökning
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        //builder.setMessage();//set station name?
        final SeekBar timeInput = new SeekBar(this);
        builder.setView(timeInput);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
}
