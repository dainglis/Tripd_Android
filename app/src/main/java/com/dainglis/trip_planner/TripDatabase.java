/*
 *  File    : TripDatabase.java
 *  Author  : David Inglis
 *  Date    : 2020-01-28
 *
 *  Defines the Room persistent storage implementation of the 'trip_planner' database
 */

package com.dainglis.trip_planner;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dainglis.trip_planner.data.TripDAO;
import com.dainglis.trip_planner.data.Trip;

@Database(entities = {Trip.class}, version = 1)
public abstract class TripDatabase extends RoomDatabase {
    private static final String databaseName = "trip_planner";

    private static TripDatabase instance;

    public static TripDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), TripDatabase.class, TripDatabase.databaseName).build();
        }

        return instance;
    }

    public abstract TripDAO tripDAO();
}
