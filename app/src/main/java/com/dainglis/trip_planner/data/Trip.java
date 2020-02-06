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
    long tripId;

    @ColumnInfo(name = "tripTitle")
    public String title;

    @ColumnInfo
    public String startLocation;

    @ColumnInfo
    public String endLocation;

    @ColumnInfo
    public String startDate;

    @ColumnInfo
    public String endDate;


    // TODO
    // add property for either direct image blob or
    // file path to image stored in local storage


    Trip(long tripId) {
        this.tripId = tripId;

    }

    @Ignore
    Trip(String title) {
        this.title = title;
    }

    @Ignore
    Trip(String title, String startLocation, String endLocation,
            String startDate, String endDate) {
        this.title = title;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setId(long id) {
        if (id > 0) {
            tripId = id;
        }
    }
    public long getId() {
        return tripId;
    }

    public String getDateStamp() {
        return startDate + "   -   " + endDate;
    }
}
