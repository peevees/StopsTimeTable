package com.mikecoding.stopstimetable;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;

public class NoFilterArrayAdapter extends ArrayAdapter{

    private Filter filter = new NoFilter();
    public ArrayList<String> items;

    @Override
    public Filter getFilter() {
        return filter;
    }

    public NoFilterArrayAdapter(Context context, int layoutResource, int textViewResourceId,
                        ArrayList<String> items) {
        super(context, layoutResource, textViewResourceId, items);
        this.items = items;
    }



    private class NoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
            result.values = items;
            result.count = items.size();
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }

}
