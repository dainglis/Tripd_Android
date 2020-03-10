
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripDAO.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the interface between the TripDatabase and the Trip data
                    object.

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dainglis.trip_planner.models.Trip;

@Dao
public interface TripDAO {

    @Query("SELECT * FROM trips")
    LiveData<List<Trip>> getAll();

    @Query("SELECT * FROM trips")
    List<Trip> getAllStatic();

    @Query("SELECT * FROM trips WHERE tripId = (:id) LIMIT 1")
    LiveData<Trip> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Trip trip);

    @Update (onConflict = OnConflictStrategy.REPLACE)
    void update(Trip trip);

    @Delete
    void delete(Trip trip);
}
