/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripInfoViewModel.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           March 14, 2020
    DESCRIPTION:    This file provides the ViewModel for the TripInfoFragment to abstract the Model
                    from the View

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;

import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.providers.TripDataContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

    Method:         getTripAsCalendarIntent()
    Description:    Generates an Intent object which will insert an event into the device's Calendar
                    content provider. The data of the current trip held by the ViewModel is provided
                    to the Intent as various Extras

    Returns:        Intent - The Intent object which will launch the default Calendar application
                        for review.
                        'null' if the Trip or ViewModel encounters an error

    --------------------------------------------------------------------------------------------- */
    public Intent getTripAsCalendarIntent() {
        if (mTrip == null || mTrip.getValue() == null) {
            return null;
        }

        Trip currentTrip = mTrip.getValue();
        DateFormat dfStart = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);
        DateFormat dfEnd = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);
        try {
            Log.d("Calendar", "Start date: " + currentTrip.getStartDate());
            Log.d("Calendar", "End date: " + currentTrip.getEndDate());
            dfStart.parse(currentTrip.getStartDate());
            dfEnd.parse(currentTrip.getEndDate());
        }
        catch (ParseException pExc) {
            String data = "Malformed event (id " + currentTrip.getId() + ")";
            Log.e("Calendar", data);
            return null;
        }

        return new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, dfStart.getCalendar().getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, dfEnd.getCalendar().getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, currentTrip.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, "Check out the Tripd app for Event details")
                .putExtra(CalendarContract.Events.EVENT_LOCATION, currentTrip.getEndLocation());
    }
}
