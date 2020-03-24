/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       CityRepo.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           March 14, 2020
    DESCRIPTION:    This file provides a singleton Object for accessing City objects

================================================================================================= */

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
