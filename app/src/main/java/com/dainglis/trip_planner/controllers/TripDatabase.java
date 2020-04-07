
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripDatabase.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the definition for the class TripDatabase, which is the
                    room persistent storage implementation of the "trip_planner" database.

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.dainglis.trip_planner.models.City;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trip.class, Event.class, City.class}, version = 2)

public abstract class TripDatabase extends RoomDatabase {

    private static final String databaseName = "trip_planner";
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Data Access Object interfaces
    public abstract TripDAO tripDAO();
    public abstract EventDAO eventDAO();
    public abstract CityDAO cityDAO();


    private static TripDatabase instance;


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         init()
        Description:    Initializes the TripDatabase singleton by calling the getInstance method,
                        then attempting to load some sample data into the database.
        Parameters:     Context         context
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    public static TripDatabase init(final Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TripDatabase.class, TripDatabase.databaseName)
                    .build();
        }

        return instance;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getInstance()
        Description:    Returns a built instance of the TripDatabase.
        Parameters:     Context         context
        Returns:        TripDatabase    instance        The TripDatabase.

    --------------------------------------------------------------------------------------------- */
    public static TripDatabase getInstance() {
        return instance;
    }

    public static List<City> initializeCities() {
        Log.d(databaseName, "Creating Cities table if it does not exist");

        List<City> lCities;

        if (instance == null) {
            return null;
        }

        lCities = instance.cityDAO().getAll();

        if (lCities == null) {
            return null;
        }
        if (lCities.size() > 0) {
            Log.d(databaseName, "Cities table exists");
            return lCities;
        }

        instance.cityDAO().insert(new City("Toronto"));
        instance.cityDAO().insert(new City("Waterloo"));
        instance.cityDAO().insert(new City("Kitchener"));
        instance.cityDAO().insert(new City("Ottawa"));
        instance.cityDAO().insert(new City("Brampton"));
        instance.cityDAO().insert(new City("Hamilton"));
        instance.cityDAO().insert(new City("Richmond Hill"));
        instance.cityDAO().insert(new City("Vaughan"));
        instance.cityDAO().insert(new City("Markham"));
        instance.cityDAO().insert(new City("Newmarket"));
        instance.cityDAO().insert(new City("Mississauga"));
        instance.cityDAO().insert(new City("Barrie"));
        instance.cityDAO().insert(new City("Niagara Falls"));
        instance.cityDAO().insert(new City("Guelph"));


        lCities = instance.cityDAO().getAll();
        Log.d(databaseName, "Added " + lCities.size() + " cities");

        return lCities;
    }


    public static void loadSampleDataFromFile(Context context, int resId) {
        if (instance == null) {
            Log.d(databaseName, "TripDatabase not instantiated");
            return;
        }

        if (instance.tripDAO().getAllStatic().size() > 0) {
            Log.d(databaseName, "TripDatabase non-empty, ignoring test data");
            return;
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getResources().openRawResource(resId)));
        String line;

        try {
            Trip currentTrip = null;
            Event currentEvent = null;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 6 && data[0].equals("0")) {
                    currentTrip = new Trip(data[1], data[2], data[3], data[4], data[5]);
                    currentTrip.setTripId(instance.tripDAO().insert(currentTrip));
                }
                else if (data.length == 3 && data[0].equals("1") && currentTrip != null) {
                    currentEvent = new Event(data[1], data[2], currentTrip.getTripId());
                    instance.eventDAO().insert(currentEvent);
                }
                else {
                    // test_data.csv is corrupted
                    return;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("All test data added successfully");
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         loadSampleData()
        Description:    Loads sample data into the TripDatabase including two Trips and five events.
        Parameters:     None.
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    public static void loadSampleData() {
        Log.d(null, "Attempting to load sample data...");

        if (instance == null) {
            Log.d(null, "Database is null");
            return;
        }

        final List<Trip> lTrips = instance.tripDAO().getAllStatic();

        if (lTrips == null || lTrips.size() > 0) {
            Log.d(null, "List is non-empty");
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
}