package com.dainglis.trip_planner;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.data.Trip;

import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Trip> {

    private int resourceLayout;
    private Context cContext;

    // by here there is no data in most of trips
    public CustomArrayAdapter(@NonNull Context context, int resource, List<Trip> trips) {
        super(context, resource, trips);
        this.resourceLayout = resource;
        this.cContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;

        if (newView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(cContext);
            newView = li.inflate(resourceLayout, null);
        }

        Trip p = getItem(position);

        if (p != null) {
            TextView tt1 = newView.findViewById(R.id.tripTitleID);
            TextView tt2 = newView.findViewById(R.id.startCityID);
            TextView tt3 = newView.findViewById(R.id.endCityID);
            TextView tt4 = newView.findViewById(R.id.startDateID);
            TextView tt5 = newView.findViewById(R.id.endDateID);

            if (tt1 != null) {
                tt1.setText(p.getTitle());
            }

            if (tt2 != null) {
                tt2.setText(p.getStartLocation());
            }

            if (tt3 != null) {
                tt3.setText(p.getEndLocation());
            }
            if (tt4 != null) {
                tt3.setText(p.getStartDate());
            }
            if (tt5 != null) {
                tt3.setText(p.getEndDate());
            }
        }

        return newView;
    }

}

