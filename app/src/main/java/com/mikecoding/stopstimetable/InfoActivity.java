package com.mikecoding.stopstimetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class InfoActivity extends AppCompatActivity {

    String siteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        siteId = getIntent().getExtras().getString("ID");
        Log.d("TAG", "siteID: " + siteId);
    }
}
