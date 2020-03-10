package com.dainglis.trip_planner.controllers;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.dainglis.trip_planner.models.City;

import java.util.List;

@Dao
public interface CityDAO {
    @Query("SELECT * FROM cities")
    List<City> getAll();

    @Query("SELECT * FROM cities WHERE cityId = (:cityId) LIMIT 1")
    City getCityById(long cityId);

    @Insert
    long insert(City city);

    @Delete
    void delete(City city);
}
