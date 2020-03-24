/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       City.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the City class. A city has a name and
                    an Id. Currently these are all Cities in the province of Ontario, Canada.

================================================================================================= */

package com.dainglis.trip_planner.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {
    @PrimaryKey(autoGenerate = true)
    public long cityId;

    @ColumnInfo(name = "cityName")
    public String name;

    public City() {}

    @Ignore
    public City(String name) {
        this.name = name;
    }
}
