
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       Event.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the Event class. An event has a title,
                    date, and associated trip ID.

================================================================================================= */

package com.dainglis.trip_planner.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "events",
        foreignKeys = @ForeignKey(
                entity = Trip.class,
                parentColumns = "tripId",
                childColumns = "tripId"),
        indices = {@Index("tripId")})

public class Event {
    @PrimaryKey(autoGenerate = true)
    public long eventId;

    @ColumnInfo(name = "tripId")
    public long tripId;

    @ColumnInfo(name = "eventTitle")
    public String title;

    @ColumnInfo(name = "eventDate")
    public String date;

    Event() {}

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         Constructor
        Description:    Instantiate an Event object with provided values for data members.

    --------------------------------------------------------------------------------------------- */
    @Ignore
    public Event(String title, String date, long tripId) {

        this.title = title;
        this.date = date;
        this.tripId = tripId;

    }

}
