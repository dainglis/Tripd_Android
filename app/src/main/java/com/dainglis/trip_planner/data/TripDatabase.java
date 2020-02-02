/*
 *  File    : TripDatabase.java
 *  Author  : David Inglis
 *  Date    : 2020-01-28
 *
 *  Defines the Room persistent storage implementation of the 'trip_planner' database
 */

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

    public static TripDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TripDatabase.class, TripDatabase.databaseName).build();
        }

        return instance;
    }

    public static boolean loadSampleData() {

        if (instance == null || instance.tripDAO().getAll().size() > 0) {
            return false;
        }

        Log.d(null, "Generating sample data");

        // First sample trip and Events
        Trip tripOne = new Trip("Santa Barbara Road Trip");
        instance.tripDAO().insert(tripOne);

        System.out.println("Trip one has ID " + tripOne.tripId);

        Event[] tripOneEvents = {
            new Event("Breakfast", tripOne.tripId),
            new Event("Lunch", tripOne.tripId),
            new Event("Dinner", tripOne.tripId)
        };


        // Second sample trip and Events
        Trip tripTwo = new Trip("Kayaking in the Mississippi");
        instance.tripDAO().insert(tripTwo);
        System.out.println("Trip two has ID " + tripTwo.tripId);

        Event[] tripTwoEvents = {
                new Event("Kayaking", tripTwo.tripId),
                new Event("Swimming", tripTwo.tripId)
        };





        for (int i = 0; i < tripOneEvents.length; i++) {
            instance.eventDAO().insert(tripOneEvents[i]);
        }
        for (int i = 0; i < tripTwoEvents.length; i++) {
            instance.eventDAO().insert(tripTwoEvents[i]);
        }

        // Insert sample data

        return true;
    }

    public abstract TripDAO tripDAO();

    public abstract EventDAO eventDAO();
}
