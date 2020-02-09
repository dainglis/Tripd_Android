
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripDatabase.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the definition for the class TripDatabase, which is the
                    room persistent storage implementation of the "trip_planner" database.

================================================================================================= */

package com.dainglis.trip_planner.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import java.util.List;

@Database(entities = {Trip.class, Event.class}, version = 1)
public abstract class TripDatabase extends RoomDatabase {
    private static final String databaseName = "trip_planner";

    private static TripDatabase instance;


    /*
     *  Method      : init
     *  Description :
     *      Initializes the TripDatabase singleton by calling the getInstance
     *      method, then attempting to load some sample data into the database
     */
    public static void init(Context context) {
        getInstance(context);
        loadSampleData();
    }

    public static TripDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TripDatabase.class, TripDatabase.databaseName).build();
        }

        return instance;
    }

    private static void loadSampleData() {

        if (instance == null || instance.tripDAO().getAll().size() > 0) {
            return;
        }

        Log.d(null, "Generating sample data");

        // First sample trip and Events
        Trip tripOne = new Trip("Santa Barbara Road Trip",
                "Glendale, CA", "Santa Barbara, CA",
                "2020-01-01", "2020-01-10");
        tripOne.setTripId(instance.tripDAO().insert(tripOne));

        //System.out.println("Trip one has ID " + tripOne.tripId);

        Event[] tripOneEvents = {
                new Event("Breakfast", "2020-01-01 00:00:00", tripOne.getId()),
                new Event("Lunch", "2020-01-01 12:00:00", tripOne.getId()),
                new Event("Dinner", "2020-01-01 19:00:00",tripOne.getId())
        };


        // Second sample trip and Events
        Trip tripTwo = new Trip("Kayaking in the Mississippi",
                "Grand Rapids, MN", "New Orleans, LA",
                "2020-05-01", "2020-05-29");
        tripTwo.setTripId(instance.tripDAO().insert(tripTwo));
        //System.out.println("Trip two has ID " + tripTwo.tripId);

        Event[] tripTwoEvents = {
                new Event("Kayaking", "2020-05-03 01:00:00",tripTwo.getId()),
                new Event("Swimming", "2020-05-01 09:00:00",tripTwo.getId()),
        };


        for (int i = 0; i < tripOneEvents.length; i++) {
            instance.eventDAO().insert(tripOneEvents[i]);
        }
        for (int i = 0; i < tripTwoEvents.length; i++) {
            instance.eventDAO().insert(tripTwoEvents[i]);
        }
        // Insert sample data
    }

    public abstract TripDAO tripDAO();

    public abstract EventDAO eventDAO();
}