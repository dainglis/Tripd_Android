/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripListViewModel.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           March 14, 2020
    DESCRIPTION:    This file provides the ViewModel for the TripListFragment to abstract the Model
                    from the View

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.dainglis.trip_planner.models.Trip;

import java.util.List;

public class TripListViewModel extends ViewModel {

    private LiveData<List<Trip>> mTrips;

    public TripListViewModel() {
        mTrips = TripDatabase.getInstance().tripDAO().getAll();
    }

    public LiveData<List<Trip>> getTrips() {
        return mTrips;
    }

}
