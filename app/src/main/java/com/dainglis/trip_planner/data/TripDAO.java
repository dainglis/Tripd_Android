/*
 *  File    : TripDAO.java
 *  Author  : David Inglis
 *  Date    : 2020-01-28
 *
 *  Interface between the TripDatabase and Trip data object
 */

package com.dainglis.trip_planner.data;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface TripDAO {

    @Query("SELECT * FROM trips")
    List<Trip> getAll();

    @Insert
    void insert(Trip trip);

    @Delete
    void delete(Trip trip);

}
