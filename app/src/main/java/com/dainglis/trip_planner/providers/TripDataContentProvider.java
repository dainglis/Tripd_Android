package com.dainglis.trip_planner.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dainglis.trip_planner.controllers.CityRepo;
import com.dainglis.trip_planner.controllers.EventDAO;
import com.dainglis.trip_planner.controllers.TripDAO;
import com.dainglis.trip_planner.controllers.TripDatabase;

public class TripDataContentProvider extends ContentProvider {

    // Content providers are awful and abstract
    // There is no need for a ContentProvider for internal use,
    // so this ContentProvider is used to provide Trip information access
    // to an external widget for the Tripd application

    // As such, it needs to only expose the query() method to "provide content"
    // to this widget

    public static final String PROVIDER = "com.dainglis.trip_planner.provider";

    private static final int TRIPS_ALL = 1;
    private static final int TRIPS_ONE = 2;
    private static final int EVENTS_ALL = 10;
    private static final int EVENTS_ONE = 11;
    private static final int CITIES_ALL = 21;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Trips

        // URI for all elements in the "trips" table
        uriMatcher.addURI(PROVIDER, "trips", TRIPS_ALL);
        // URI for a single element in the "trips" table, by Id
        uriMatcher.addURI(PROVIDER, "trips/#", TRIPS_ONE);

        // Events

        // URI for all elements in the "events" table
        uriMatcher.addURI(PROVIDER, "events", EVENTS_ALL);
        // URI for a single element in the "events" table, by Id
        uriMatcher.addURI(PROVIDER, "events/#", EVENTS_ONE);

        uriMatcher.addURI(PROVIDER, "cities", CITIES_ALL);

    }

    private TripDAO tripDAO;
    private EventDAO eventDAO;

    @Override
    public boolean onCreate() {

        Log.d("TDCP", "Initializing trip database content provider");

        TripDatabase tripDB = TripDatabase.init(getContext());

        tripDAO = tripDB.tripDAO();
        eventDAO = tripDB.eventDAO();

        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                CityRepo.cities = TripDatabase.initializeCities();
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] proj, @Nullable String sel, @Nullable String[] selArgs, @Nullable String sort) {
        switch (uriMatcher.match(uri)) {
            case TRIPS_ALL:
                return tripDAO.getAllAsCursor();

            case TRIPS_ONE:

                if (uri.getLastPathSegment() != null) {
                    long id = 0;
                    try {
                        id = Long.parseLong(uri.getLastPathSegment());
                        return tripDAO.getByIdAsCursor(id);
                    } catch (Exception e) {
                        Log.e("TDCP", "Malformed URI: " + uri.toString());
                    }
                }
                break;
            default:
                Log.e("TDCP", "Unknown URI: " + uri.toString());

        }
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
        // At this time data deletion is not supported
        return 0;
    }

}
