/*
 *  File    : Trip.java
 *  Author  : David Inglis
 *  Date    : 2020-01-28
 *
 *  Defines a Trip data object for the Trip Planner application. A single Trip
 *  can have one or more Days
 */

package com.dainglis.trip_planner.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "trips",
        indices = {@Index("tripId")})
public class Trip {
    @PrimaryKey(autoGenerate = true)
    public int tripId;

    @ColumnInfo(name = "tripTitle")
    public String title;

    @ColumnInfo
    public String startLocation;

    @ColumnInfo
    public String endLocation;


    Trip(int tripId) {
        this.tripId = tripId;

    }

    @Ignore
    Trip(String title) {
        this.title = title;
    }

    @Ignore
    Trip(String title, String startLocation, String endLocation) {
        this.title = title;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }
}
