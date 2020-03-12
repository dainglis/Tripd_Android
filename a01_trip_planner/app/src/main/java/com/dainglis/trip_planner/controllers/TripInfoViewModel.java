package com.dainglis.trip_planner.controllers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripInfoViewModel extends ViewModel {

    public static final String KEY_MAIN_TEXT = "main";
    public static final String KEY_SECONDARY_TEXT = "secondary";

    private long mTripId;
    private LiveData<Trip> mTrip;
    private LiveData<List<Event>> mTripEvents;

    public TripInfoViewModel() {
        mTripId = 0;
    }

    public void setTripId(long tripId) {
        mTripId = tripId;
        mTrip = TripDatabase.getInstance().tripDAO().getById(mTripId);
        mTripEvents = TripDatabase.getInstance().eventDAO().getAllByTripId(mTripId);
    }

    public long getTripId() {
        return mTripId;
    }

    public LiveData<Trip> getTrip() { return mTrip; }

    public LiveData<List<Event>> getEvents() { return mTripEvents; }

     /* METHOD HEADER COMMENT -----------------------------------------------------------------------

    Method:         generateEventInfoList()
    Description:    Queries the local database for all Events matching the specified tripId.
    Parameters:     long        tripId      The id of the Trip to query the database for and
                                            populate the layout elements with the Trip data.
    Returns:        List<Map<String, String>>   The requested data from the database.

    --------------------------------------------------------------------------------------------- */
    public List<Map<String, String>> generateEventInfoList(List<Event> events) {
        List<Map<String, String>> data = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            Map<String, String> datum = new HashMap<>(2);
            datum.put(KEY_MAIN_TEXT, events.get(i).title);
            datum.put(KEY_SECONDARY_TEXT,events.get(i).date);
            data.add(datum);
        }

        return data;
    }
}
