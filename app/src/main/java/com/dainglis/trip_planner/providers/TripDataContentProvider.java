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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TripDataContentProvider extends ContentProvider {

    // Content providers are awful and abstract
    // There is no need for a ContentProvider for internal use,
    // so this ContentProvider is used to provide Trip information access
    // to an external widget for the Tripd application

    // As such, it needs to only expose the query() method to "provide content"
    // to this widget


    private static final int TRIPS_ALL = 1;
    private static final int TRIPS_ONE = 2;
    private static final int TRIPS_BY = 3;
    private static final int TRIPS_NOW = 4;
    private static final int EVENTS_ALL = 10;
    private static final int EVENTS_ONE = 11;
    private static final int EVENTS_BY = 12;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // Trips

        // URI for all elements in the "trips" table
        uriMatcher.addURI(TripDataContract.AUTHORITY, "trips", TRIPS_ALL);
        // URI for a single element in the "trips" table, by Id
        uriMatcher.addURI(TripDataContract.AUTHORITY, "trips/#", TRIPS_ONE);
        // URI for a single element in the "trips table, next trip starting after a specified date
        uriMatcher.addURI(TripDataContract.AUTHORITY, "trips/by", TRIPS_BY);
        // URI for a single element in the "trips table, first trip that is active on a specified date
        uriMatcher.addURI(TripDataContract.AUTHORITY, "trips/now", TRIPS_NOW);

        // Events

        // URI for all elements in the "events" table
        uriMatcher.addURI(TripDataContract.AUTHORITY, "events", EVENTS_ALL);
        // URI for a single element in the "events" table, by Id
        uriMatcher.addURI(TripDataContract.AUTHORITY, "events/#", EVENTS_ONE);
        // URI for all elements in the "events" table matching the tripId
        uriMatcher.addURI(TripDataContract.AUTHORITY, "events/by/#", EVENTS_BY);


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

            case TRIPS_BY:
                if (sel != null) {
                    DateFormat dateFormat = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);
                    try {
                        dateFormat.parse(sel);
                        Log.d("TDCP", "Date received: " + sel);

                        Cursor c = tripDAO.getNextTripAfterDate(sel);
                        return c;
                    } catch (ParseException exc) {
                        Log.e("TDCP", "Malformed selection string: " + sel);
                    }
                }
                break;

            case TRIPS_NOW:
                if (sel != null) {
                    DateFormat dateFormat = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);
                    try {
                        dateFormat.parse(sel);

                        return tripDAO.getFirstActiveTrip(sel);
                    } catch (ParseException exc) {
                        Log.e("TDCP", "Malformed selection string: " + sel);
                    }
                }
                break;

            case EVENTS_ALL:
                return eventDAO.getAllAsCursor();

            case EVENTS_BY:
                if (uri.getLastPathSegment() != null) {
                    long id = 0;
                    try {
                        id = Long.parseLong(uri.getLastPathSegment());
                        return eventDAO.getAllByTripIdAsCursor(id);
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
