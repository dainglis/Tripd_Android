
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       EventDAO.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           January 28th, 2020
    DESCRIPTION:    This file contains the interface for between the TripDatabase and the EventDAO
                    data object.

================================================================================================= */

package com.dainglis.trip_planner.controllers;

import java.util.List;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAll();

    @Query("SELECT * FROM events WHERE tripId = (:tripId)")
    LiveData<List<Event>> getAllByTripId(long tripId);

    @Query("SELECT * FROM events WHERE tripId = (:tripId)")
    List<Event> getAllByTripIdStatic(long tripId);

    @Insert
    long insert(Event event);

    @Delete
    void delete(Event event);

}
