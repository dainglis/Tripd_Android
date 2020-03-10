
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripListAdapter.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the TripListAdapter class. This adapter
                    takes a Trip object and formats it to follow the card_view_list_item layout
                    within a list.

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.views.TripListFragment;

import java.util.List;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {

    class TripViewHolder extends RecyclerView.ViewHolder {

        private final TextView tripTitle;
        private final TextView tripStartCity;
        private final TextView tripEndCity;
        private final TextView tripStartDate;
        private final TextView tripEndDate;

        private TripViewHolder(View itemView) {
            super(itemView);
            tripTitle = itemView.findViewById(R.id.tripTitleID);
            tripStartCity = itemView.findViewById(R.id.startCityID);
            tripEndCity = itemView.findViewById(R.id.endCityID);
            tripStartDate = itemView.findViewById(R.id.startDateID);
            tripEndDate = itemView.findViewById(R.id.endDateID);

        }
    }

    private final TripListFragment mParent;
    private final LayoutInflater mInflater;
    private List<Trip> mTrips;


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         TripListAdapter()        -- Constructor
        Description:    Instantiates a TripListAdapter object.
        Parameters:     Context     context         The context from the calling function.
                        int         resource        The resource layout provided
                        List<Trip>  trips           The list of current trips
        Returns:        N/A

    --------------------------------------------------------------------------------------------- */
    public TripListAdapter(@NonNull Context context, TripListFragment parent) {
        mInflater = LayoutInflater.from(context);
        mParent = parent;
    }

    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_view_list_item, parent, false);

        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripViewHolder holder, int position) {
        if (mTrips != null) {
            Trip current = mTrips.get(position);
            holder.tripTitle.setText(current.getTitle());
            holder.tripStartCity.setText(current.getStartLocation());
            holder.tripEndCity.setText(current.getEndLocation());
            holder.tripStartDate.setText(current.getStartDate());
            holder.tripEndDate.setText(current.getEndDate());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.raiseTripSelected(mTrips.get(holder.getAdapterPosition()).getId());

                }
            });
        } else {
            holder.tripTitle.setText("NULL TRIP");
        }
    }

    public void setTrips(List<Trip> trips) {
        mTrips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTrips != null) {
            return mTrips.size();
        }

        return 0;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getView()        -- Override
        Description:    Creates a view for a row of the TripListAdapter list.
        Parameters:     int         position        The row to be created
                        View        convertView     The view to be created
                        ViewGroup   parent          The type of view to be adapted.
        Returns:        View        newView         The newly adapted view.

    --------------------------------------------------------------------------------------------- */
    /*
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
    */

}

