
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripFormActivity.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    The SetupPage Activity presents the fields required to create a trip/
                    This file contains the classes and methods used to save a trip and save it
                    to the database.

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class TripFormActivity extends AppCompatActivity {

    // Create vars for cancel and confirm buttons
    Button CanButt;
    Button BtnConfirm;

    // Create vars for edit texts
    EditText EditName;
    EditText EditStartCity;
    EditText EditEndCity;
    EditText DateDepartEnter;
    EditText DateArriveEnter;
    public long currentTripId = 0;
    Bundle extras;

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getCurrentTrip()        -- Override
        Description:    Executes and prepares the creation of the Setup Page Activity.
        Parameters:     Bundle      savedInstanceState
        Returns:        void

    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditName = findViewById(R.id.editName);
        EditStartCity = findViewById(R.id.editStartCity);
        EditEndCity = findViewById(R.id.editEndCity);
        DateDepartEnter = findViewById(R.id.dateDepartEnter);
        DateArriveEnter = findViewById(R.id.dateArriveEnter);

        // create action for cancel button
        CanButt = findViewById(R.id.buttonSetupCancel);

        CanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create addBtn listener
                cancelForm();
                //openActivity();

            }
        });

        BtnConfirm = findViewById(R.id.buttonSetupConfirm); // create action for confirm button

        BtnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //create addBtn listener
                if( !fieldValidation(EditName) || !fieldValidation(EditStartCity) || !fieldValidation(EditEndCity)||
                        !dateValidate(DateDepartEnter) || !dateValidate(DateArriveEnter) )
                {
                }else
                submitForm();

            }

        });

        extras = getIntent().getExtras();

        if (extras == null) { }
        else {

            currentTripId = extras.getLong("tripId");

            if (currentTripId != 0) {

                // Query the database and update the layout in a secondary thread
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {
                        loadTripDetails(currentTripId);
                    }

                });

            }
        }

    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         cancelForm()
        Description:    Cancels the form for creating a new Trip, telling the calling activity that
                        it was cancelled.
        Parameters:     void.
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    private void cancelForm() {
        setResult(RESULT_CANCELED);
        finish();
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         openActivity()
        Description:    This method is called when the add button is pressed.
                        When pressed, main page is called.
        Parameters:     None.
        Returns:        Void.

    --------------------------------------------------------------------------------------------- */
    public void openActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         submitForm()
        Description:    Submit the form for creating / editing a new Trip.
                        If there are extras, it means that the form action is updating.
                        If there are no extras, it means that the action is creation/insertion.
        Parameters:     void.
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    private void submitForm() {
        String title = EditName.getText().toString();
        String startLocation = EditStartCity.getText().toString();
        String endLocation = EditEndCity.getText().toString();
        String startDate = DateDepartEnter.getText().toString();
        String endDate = DateArriveEnter.getText().toString();

        final Trip trip = new Trip(title, startLocation, endLocation, startDate, endDate);

        try {

            if (extras == null) {

                // Save the new trip to the db in a background thread,
                // then return to the calling Activity
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {
                        TripDatabase.getInstance().tripDAO().insert(trip);
                        //      Toast.makeText(TripFormActivity.this,"Trip saved", Toast.LENGTH_SHORT).show();
                    }

                });

            } else {

                trip.setTripId(currentTripId);
                AsyncTask.execute(new Runnable() {

                    @Override
                    public void run() {
                        TripDatabase.getInstance().tripDAO().update(trip);
                        //     Toast.makeText(TripFormActivity.this,"Trip edited", Toast.LENGTH_SHORT).show();
                    }

                });

            }

            setResult(RESULT_OK);
        }

        // this is bad practice
        catch (Exception e) {
            setResult(RESULT_CANCELED);
        }

        finish();

    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         dateValidate()
        Description:    This method is called when the add button is triggered. Validating the
                        date entered by the user.
        Parameters:     EditText        EditTextDate        Date string.
        Returns:        boolean         True                If there are no exceptions thrown
                                        False               If an exception is caught.

    --------------------------------------------------------------------------------------------- */
    public boolean dateValidate(EditText EditTextDate)
    {
        // set the date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date ObjDate = null;
        df.setLenient(false);

        // use try catch to compare string with df format
        try
        {
            ObjDate = df.parse(EditTextDate.getText().toString());
            return true;
        }
        catch(Exception e)
        {
            Toast.makeText(TripFormActivity.this,EditTextDate.getText().toString() + " is not a valid date.", Toast.LENGTH_LONG).show();
            return false;

        }
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         fieldValidation()
        Description:    This method is called when the add / edit button is pressed. This
                        method validates the input fields as entered by the user
        Parameters:     EditText        editText        The text to be verified.
        Returns:        boolean         False           If the form is incomplete
                                        True            If the form is complete

    --------------------------------------------------------------------------------------------- */
    public boolean fieldValidation(EditText editText)
    {
        if (editText.getText().toString() == null || editText.getText().toString().trim().length() < 1){

            Toast.makeText(TripFormActivity.this,"Fill the complete trip information", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         loadTripDetails()
        Description:    If there were "Extras", given a unique ID for a Trip, the details of the
                        Trip are retrieved from the "trip_planner" database.
        Parameters:     long        id      The unique id of the requested Trip object.
        Returns:        void.

    --------------------------------------------------------------------------------------------- */
    protected void loadTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId);

        if (currentTrip != null) {
            // Edit texts to be populated

            EditName.setText(currentTrip.getTitle());
            EditStartCity.setText(currentTrip.getStartLocation());
            EditEndCity.setText(currentTrip.getEndLocation());
            DateDepartEnter.setText(currentTrip.getStartDate());
            DateArriveEnter.setText(currentTrip.getEndDate());

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
        return null;
        //return TripDatabase.getInstance().tripDAO().getById(id);
    }

}

