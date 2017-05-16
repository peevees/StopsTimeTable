package com.mikecoding.stopstimetable;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InformationAdapter extends ArrayAdapter<Information> {

    Context context;
    int layoutResourceId;
    ArrayList<Information> informations = null;

    public InformationAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Information> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourceId = resource;
        this.informations = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder vh = null;

        if (row == null) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            vh = new ViewHolder();
            vh.info = (TextView) row.findViewById(R.id.tv_info);

            row.setTag(vh);

        }
        else {
            vh = (ViewHolder) row.getTag();
        }

        Information information = informations.get(position);

        if (information.getEmptyMessage() != 0) {
            vh.info.setText(information.getEmptyMessage());

        } else {
            vh.info.setText(information.getDisplayTime() + ", " + information.getGroupOfLine() + " " + information.getLineNumber() + " mot " +
                    information.getDestination());
        }

        return row;

    }
    private class ViewHolder {
        public TextView info;
    }
}

