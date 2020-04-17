
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
import android.database.Cursor;

import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

@Dao
public interface EventDAO {

    String QUERY_ALL = "SELECT * FROM events";
    String QUERY_ALL_BY_ID = "SELECT * FROM events WHERE tripId = (:tripId) ORDER BY eventDate ASC";


    @Query(QUERY_ALL)
    LiveData<List<Event>> getAll();

    @Query(QUERY_ALL)
    List<Event> getAllStatic();

    @Query(QUERY_ALL)
    Cursor getAllAsCursor();


    @Query(QUERY_ALL_BY_ID)
    LiveData<List<Event>> getAllByTripId(long tripId);

    @Query(QUERY_ALL_BY_ID)
    List<Event> getAllByTripIdStatic(long tripId);

    @Query(QUERY_ALL_BY_ID)
    Cursor getAllByTripIdAsCursor(long tripId);

    @Insert
    long insert(Event event);

    @Delete
    void delete(Event event);

}
