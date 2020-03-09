
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       EventFormActivity.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the EventFormActivity class.
                    An object of this class has a Trip, event title, event date, and allows the
                    user to edit, or save an event.

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.controllers.TripDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
*   Class:          EventFormActivity
*
*   Members:
*   private:        Button btnCancel
*                   Button btnConfirm
*                   long currentTripId
*                   Trip currentTrip
*                   EditText eventTitle
*                   EditText eventDate
*
*   Methods:
*   private:        getCurrentTrip()
*                   cancelForm()
*                   validateEventForm()
*                   saveEventForm()
*
*
*/
public class EventFormActivity extends AppCompatActivity {

    /*Declare variables*/
    Button btnCancel;
    Button btnConfirm;
    long currentTripId = 0;
    Trip currentTrip;
    EditText eventTitle;
    EditText eventDate;
    Bundle extras;
    long addTripEvent;

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         onCreate()        -- Override
        Description:    Creates and populates an Event Form Activity.
        Parameters:     Bundle      savedInstanceState
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*Get trip data members from database*/
        //currentTrip = getCurrentTrip(currentTripId);

        /*Connect title and date variables to EditText fields*/
        eventTitle = findViewById(R.id.eventTitleEdit);
        eventDate = findViewById(R.id.eventDateEdit);

        //Connect Cancel button and set click listener
        btnCancel = findViewById(R.id.buttonEventFormCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelForm();
            }
        });

        // Connect Confirm button and set click listener
        btnConfirm = findViewById(R.id.buttonEventFormConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*validate non-empty title and valid date within trip date range*/
                if (validateEventForm()) {
                    saveEventForm();
                }
            }
        });

        /*Get tripId passed when Add Event Activity invoked*/
        extras = getIntent().getExtras();

        if (extras != null) {
            currentTripId = extras.getLong("tripId");

            if (currentTripId == 0) {
                cancelForm();
            }
        }

    }




    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getCurrentTrip()
        Description:    Queries the `trip_planner` database for the Trip object with the specified
                        id.
        Parameters:     long        id      The unique id of the requested Trip object.
        Returns:        Trip                The Trip object corresponding to the provided Id

    --------------------------------------------------------------------------------------------- */
    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance().tripDAO().getById(id);
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         cancelForm()
        Description:    Cancels the form for creating a new Trip, telling the calling activity
                        that it was cancelled.
        Parameters:     void
        Returns:        void;       Sets result to RESULT_CANCELED

    --------------------------------------------------------------------------------------------- */
    private void cancelForm() {
        setResult(RESULT_CANCELED);
        finish();
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         validateEventForm()
        Description:    Validates the EditText fields of the form's layout, ensuring the "Title" is
                        a valid string, and the "DateTime" is in the correct DateTime format:
                        i.e. YYYY-MM-DD HH:MM:SS
        Parameters:     void
        Returns:        boolean         True            If the form is valid
                                        False           If the form is invalid.

    --------------------------------------------------------------------------------------------- */
    private boolean validateEventForm() {

        // set the date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setLenient(false);

        // Ensure Event title is not empty
        if (eventTitle.getText().length() == 0) {
            Toast.makeText(getApplicationContext(),
                    "Event title cannot be blank",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // use try catch to compare string with df format
        try {

            df.parse(eventDate.getText().toString());

            /*

            if ( ObjDate.before( df.parse(currentTrip.getStartDate())) || ObjDate.after( df.parse(currentTrip.getEndDate())))
            {
                Toast.makeText(getApplicationContext(), "Invalid Date Entered", Toast.LENGTH_SHORT).show();
                return false;
            }
            */
        }
        catch(Exception e)
        {
            Toast.makeText(getApplicationContext(),
                    "Date is not in the correct format",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         saveEventForm()
        Description:    Attempts to save the form as a new Event in the database
        Parameters:     void.
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    private void saveEventForm() {
        final Event event =
                new Event(eventTitle.getText().toString(),
                        eventDate.getText().toString(),
                        currentTripId);
        try {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    TripDatabase.getInstance().eventDAO().insert(event);
                }
            });

            setResult(RESULT_OK);
            finish();
        }
        catch(Exception e)
        {
            Toast.makeText(EventFormActivity.this, "Error adding event", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);

        }
    }
}
