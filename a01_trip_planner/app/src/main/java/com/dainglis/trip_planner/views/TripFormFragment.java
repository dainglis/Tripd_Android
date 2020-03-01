
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripFormFragment.java
    PROJECT:        PROG3150 - Assignment 02
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 28th, 2020
    DESCRIPTION:    The SetupPage Fragment presents the fields required to create a trip/
                    This file contains the classes and methods used to save a trip and save it
                    to the database.

================================================================================================= */


package com.dainglis.trip_planner.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.controllers.TripDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




public class TripFormFragment extends Fragment {


    public TripFormFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_form, container, false);
    }
}
