package com.dainglis.trip_planner;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomCardListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] tripName;
    private final String[] tripStartCity;
    private final String[] tripEndCity;
    private final String[] tripStartDate;
    private final String[] tripEndDate;

    public CustomCardListAdapter(Activity context, String[] newTripName, String[] newTripStartCity, String[] newTripEndCity, String[] newTripStartDate, String[] newTripEndDate) {
        super(context, R.layout.card_view_list_item, newTripName);

        this.context=context;
        this.tripName = newTripName;
        this.tripStartCity = newTripStartCity;
        this.tripEndCity = newTripEndCity;
        this.tripStartDate = newTripStartDate;
        this.tripEndDate = newTripEndDate;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.card_view_list_item, null,true);

        TextView tripNameView = rowView.findViewById(R.id.tripTitleID);
        TextView startLocationView = rowView.findViewById(R.id.startCityNameID);
        TextView endLocationView = rowView.findViewById(R.id.endCityNameID);
        TextView tripStartDateView = rowView.findViewById(R.id.tripStartDateID);
        TextView tripEndDateView = rowView.findViewById(R.id.tripEndDateID);

        tripNameView.setText(tripName[position]);
        startLocationView.setText(tripStartCity[position]);
        endLocationView.setText(tripEndCity[position]);
        tripStartDateView.setText(tripStartDate[position]);
        tripEndDateView.setText(tripEndDate[position]);

        return rowView;

    }
}
