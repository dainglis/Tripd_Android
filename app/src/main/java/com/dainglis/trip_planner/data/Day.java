/*
 *  File    : Day.java
 *  Author  : David Inglis
 *  Date    : 2020-01-28
 *
 *  Defines a Day data object for the Trip Planner application. A single Day can
 *  have one or more Events
 */

package com.dainglis.trip_planner.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Day {
    @PrimaryKey
    public int dayId;

    @ColumnInfo(name = "date")
    public String date;
}
