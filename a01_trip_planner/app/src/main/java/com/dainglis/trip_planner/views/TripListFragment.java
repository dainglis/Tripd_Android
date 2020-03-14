/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripListFragment.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           March 10th, 2020
    DESCRIPTION:    The TripListFragment presents a list of all Trips in the TripDatabase
                    Selecting a single Trip from the list signals the calling Activity to launch
                    an informational page for the Trip

 */

package com.dainglis.trip_planner.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripListAdapter;
import com.dainglis.trip_planner.controllers.TripListViewModel;
import com.dainglis.trip_planner.models.Trip;

import java.util.List;

public class TripListFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TripListAdapter tAdapter;
    private TripListViewModel mViewModel;
    private FloatingActionButton mAddButton;


    public static TripListFragment newInstance() {
        return new TripListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_list, container, false);


        RecyclerView mRecyclerView = view.findViewById(R.id.trip_card_rec_view);
        tAdapter = new TripListAdapter(view.getContext(), this);

        mRecyclerView.setAdapter(tAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        // create action for add button
        mAddButton = view.findViewById(R.id.addBtn);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TripListViewModel.class);

        mViewModel.getTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable List<Trip> trips) {
                tAdapter.setTrips(trips);
            }
        });

        //create addBtn listener
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseAddTrip();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripListFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void raiseTripSelected(long tripId) {
        if (mListener != null) {
            mListener.onTripSelected(tripId);
        }
    }

    public void raiseAddTrip() {
        if (mListener != null) {
            mListener.onAddButtonPressed();
        }
    }


    // Interface for interaction with the Activity that owns the TripListFragment instance
    public interface OnFragmentInteractionListener {
        void onTripSelected(long tripId);
        void onAddButtonPressed();
    }
}
