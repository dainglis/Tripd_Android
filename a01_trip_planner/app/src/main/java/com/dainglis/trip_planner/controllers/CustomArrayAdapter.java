
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       CustomArrayAdapter.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the CustomArrayAdapter class. This adapter
                    takes a Trip object and formats it to follow the card_view_list_item layout
                    within a list.

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<Trip> {

    private int resourceLayout;
    private Context cContext;
    private List<Trip> Trips;

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         CustomArrayAdapter()        -- Constructor
        Description:    Instantiates a CustomArrayAdapter object.
        Parameters:     Context     context         The context from the calling function.
                        int         resource        The resource layout provided
                        List<Trip>  trips           The list of current trips
        Returns:        N/A

    --------------------------------------------------------------------------------------------- */
    public CustomArrayAdapter(@NonNull Context context, int resource, List<Trip> trips) {
        super(context, resource, trips);
        this.resourceLayout = resource;
        this.cContext = context;
        Trips = trips;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getView()        -- Override
        Description:    Creates a view for a row of the CustomArrayAdapter list.
        Parameters:     int         position        The row to be created
                        View        convertView     The view to be created
                        ViewGroup   parent          The type of view to be adapted.
        Returns:        View        newView         The newly adapted view.

    --------------------------------------------------------------------------------------------- */
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

