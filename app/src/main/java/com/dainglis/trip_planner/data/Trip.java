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
    private
    long tripId;

    @ColumnInfo(name = "tripTitle")
    private String title;

    @ColumnInfo
    private String startLocation;

    @ColumnInfo
    private String endLocation;

    @ColumnInfo
    private String startDate;

    @ColumnInfo
    private String endDate;


    // TODO
    // add property for either direct image blob or
    // file path to image stored in local storage


    Trip(long tripId) {
        this.setTripId(tripId);

    }

    @Ignore
    Trip(String title) {
        this.setTitle(title);
    }

    @Ignore
    public Trip(String title, String startLocation, String endLocation,
            String startDate, String endDate) {
        this.setTitle(title);
        this.setStartLocation(startLocation);
        this.setEndLocation(endLocation);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public void setId(long id) {
        if (id > 0) {
            setTripId(id);
        }
    }
    public long getId() {
        return getTripId();
    }

    public String getDateStamp() {
        return getStartDate() + "   -   " + getEndDate();
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
