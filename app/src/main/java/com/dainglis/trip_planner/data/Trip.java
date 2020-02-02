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
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Trip {
    @PrimaryKey
    public int tripId;

    @ColumnInfo(name = "trip_title")
    public String title;
}
