package com.dainglis.trip_planner.data;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface EventDAO {

    @Query("SELECT * FROM events")
    List<Event> getAll();

    @Query("SELECT * FROM events WHERE tripId = (:tripId)")
    List<Event> getAllByTripId(long tripId);

    @Insert
    long insert(Event event);

    @Delete
    void delete(Event event);

}
