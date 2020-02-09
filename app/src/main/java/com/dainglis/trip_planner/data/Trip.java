
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       Trip.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the definition of a Trip data object for the trip planner
                    application. A single Trip has an ID, a title, a start and end location, and
                    a start and end date.

================================================================================================= */

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

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         Constructor
        Description:    Instantiate a Trip object with provided values for data members.

    --------------------------------------------------------------------------------------------- */
    @Ignore
    public Trip(String title, String startLocation, String endLocation,
            String startDate, String endDate) {

        this.setTitle(title);
        this.setStartLocation(startLocation);
        this.setEndLocation(endLocation);
        this.setStartDate(startDate);
        this.setEndDate(endDate);

    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         Setters / Mutators
        Description:    Validate and set the various members of the Trip class.

    --------------------------------------------------------------------------------------------- */
    public void setId(long id) {
        if (id > 0) {
            setTripId(id);
        }
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartLocation(String startLocation) {this.startLocation = startLocation; }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         Getters / Accessors
        Description:    Publicly access the various members of the Trip class.

    --------------------------------------------------------------------------------------------- */

    public long getId() {
        return getTripId();
    }

    public String getDateStamp() {
        return getStartDate() + "   -   " + getEndDate();
    }

    public long getTripId() {
        return tripId;
    }

    public String getTitle() {
        return title;
    }

    public String getStartLocation() {
        return startLocation;
    }

     public String getEndLocation() {
        return endLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
