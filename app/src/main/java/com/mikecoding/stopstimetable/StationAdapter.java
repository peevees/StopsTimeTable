package com.mikecoding.stopstimetable;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StationAdapter extends ArrayAdapter<Station> {
    Context context;
    int layoutResourceId;
    ArrayList<Station> stations = null;

    public StationAdapter(Context context, int layoutResourceId, ArrayList<Station> stations) {
        super(context, layoutResourceId, stations);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.stations = (ArrayList) stations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        StationHolder holder = null;

        if (row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new StationHolder();
            holder.name = (TextView) row.findViewById(R.id.name);
            row.setTag(holder);
        }else{
            holder = (StationHolder) row.getTag();
        }
        Station item = stations.get(position);
        holder.name.setText(item.getName());
        return row;
    }

    private class StationHolder {
        public TextView name;
    }
}