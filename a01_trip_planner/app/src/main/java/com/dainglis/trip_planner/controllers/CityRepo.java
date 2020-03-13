package com.dainglis.trip_planner.controllers;

import com.dainglis.trip_planner.models.City;

import java.util.List;

public class CityRepo {
    public static List<City> cities;

    public static boolean init(TripDatabase db) {
        cities = db.cityDAO().getAll();
        return true;
    }

    public static City get(int i) {
        if (cities == null) {
            return null;
        }

        return cities.get(i);
    }

    public static String getName(int i) {
        if (cities == null) {
            return null;
        }
        return cities.get(i).name;
    }

    public static int size() {
        return cities.size();
    }
}
