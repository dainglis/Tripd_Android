package com.dainglis.trip_planner.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.CityRepo;
import com.dainglis.trip_planner.controllers.EventDAO;
import com.dainglis.trip_planner.controllers.TripDAO;
import com.dainglis.trip_planner.controllers.TripDatabase;

public class TripDataContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.dainglis.trip_planner.providers.TripDataContentProvider";

    private static final int TRIPS_ALL = 1;
    private static final int TRIPS_ONE = 2;
    private static final int EVENTS_ALL = 10;
    private static final int EVENTS_ONE = 11;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Trips

        // URI for all elements in the "trips" table
        uriMatcher.addURI(PROVIDER_NAME, "trips", TRIPS_ALL);
        // URI for a single element in the "trips" table, by Id
        uriMatcher.addURI(PROVIDER_NAME, "trips/#", TRIPS_ONE);

        // Events

        // URI for all elements in the "events" table
        uriMatcher.addURI(PROVIDER_NAME, "events", EVENTS_ALL);
        // URI for a single element in the "events" table, by Id
        uriMatcher.addURI(PROVIDER_NAME, "events/#", EVENTS_ONE);

    }

    private TripDatabase tripDatabase;
    private TripDAO tripDAO;
    private EventDAO eventDAO;

    @Override
    public boolean onCreate() {

        Log.d("tripDB", "Initializing trip database content provider");

        tripDatabase = TripDatabase.init(getContext());

        tripDAO = tripDatabase.tripDAO();
        eventDAO = tripDatabase.eventDAO();

        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {


                CityRepo.cities = TripDatabase.initializeCities();
                TripDatabase.loadSampleDataFromFile(getContext(), R.raw.test_data);
            }
        });

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
