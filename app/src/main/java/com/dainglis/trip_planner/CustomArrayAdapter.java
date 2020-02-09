package com.dainglis.trip_planner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.dainglis.trip_planner.data.Trip;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Trip> {

    private int resourceLayout;
    private Context cContext;
    private List<Trip> Trips;

    // by here there is no data in most of trips
    public CustomArrayAdapter(@NonNull Context context, int resource, List<Trip> trips) {
        super(context, resource, trips);
        this.resourceLayout = resource;
        this.cContext = context;
        Trips = trips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View newView = convertView;

        if (newView == null) {
            LayoutInflater li;
            li = LayoutInflater.from(cContext);
            newView = li.inflate(resourceLayout, null);
        }

        Trip p = Trips.get(position);

        if (p != null) {
            TextView tv1 = newView.findViewById(R.id.tripTitleID);
            TextView tv2 = newView.findViewById(R.id.startCityID);
            TextView tv3 = newView.findViewById(R.id.endCityID);
            TextView tv4 = newView.findViewById(R.id.startDateID);
            TextView tv5 = newView.findViewById(R.id.endDateID);

            if (tv1 != null) {
                tv1.setText(p.getTitle());
            }
            if (tv2 != null) {
                tv2.setText(p.getStartLocation());
            }
            if (tv3 != null) {
                tv3.setText(p.getEndLocation());
            }
            if (tv4 != null) {
                tv4.setText(p.getStartDate());
            }
            if (tv5 != null) {
                tv5.setText(p.getEndDate());
            }
        }

        return newView;
    }

}

