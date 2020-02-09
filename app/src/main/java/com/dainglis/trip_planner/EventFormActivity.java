package com.dainglis.trip_planner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Event;
import com.dainglis.trip_planner.data.EventDAO;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private Button btnCancel;
    private Button btnConfirm;
    private long currentTripId = 0;
    private Trip currentTrip;
    private EditText eventTitle;
    private EditText eventDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Get tripId passed when Add Event Activity invoked*/
        Bundle extras = getIntent().getExtras();
        currentTripId = extras.getLong("tripId");

        /*Get trip data members from database*/
        currentTrip = getCurrentTrip(currentTripId);

        /*Connect title and date variables to EditText fields*/
        eventTitle = (EditText)findViewById(R.id.eventTitleEdit);
        eventDate = (EditText)findViewById(R.id.eventDateEdit);

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
                if (validateEventForm(eventTitle, eventDate, currentTrip)) {
                    saveEventForm();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Check Event Name and Dates For Valid Input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /*
    *  Method      : getCurrentTrip
    *  Description : Queries the `trip_planner` database for the Trip object with the specified id
    *  Parameters  : long id : The unique id of the requested Trip
    *  Returns     : Trip : The Trip object with the corresponding id, or null
    */
    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance(getApplicationContext()).tripDAO().getById(id);
    }

    /*
    *  Method      : cancelForm
    *  Description : Cancels the form for creating a new Trip, telling
    *                  the calling activity that it was cancelled.
    *  Parameters  : void
    *  Returns     : void; set result to RESULT_CANCELED (== 0)
    */
    private void cancelForm() {
        setResult(RESULT_CANCELED);
        finish();
    }

    /*
    *  Method      : validateEventForm
    *  Description : Validates the EditText fields of the form's layout, ensuring
    *                   the "Title" is a valid string, and the "DateTime" is in
    *                   the correct DateTime format: YYYY-MM-DD HH:MM:SS
    *  Parameters  : void
    *  Returns     : boolean; `true` if the form is valid, `false` otherwise
    */
    private boolean validateEventForm(EditText title, EditText date, Trip trip) {

        // set the date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date ObjDate = null;
        df.setLenient(false);

        // use try catch to compare string with df format
        try
        {

            ObjDate = df.parse(eventDate.toString());

            if ( ObjDate.before( df.parse(currentTrip.getStartDate())) || ObjDate.after( df.parse(currentTrip.getEndDate())))
            {
                Toast.makeText(getApplicationContext(), "Invalid Date Entered", Toast.LENGTH_SHORT).show();
                return false;
            }

            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /*
    *  Method      : saveEventForm
    *  Description : Attempts to save the form as a new Event in the database
    *  Parameters  : void
    *  Returns     : void
    */
    private void saveEventForm() {

        long addTripEvent;
        final Event newEvent = new Event(eventTitle.toString(),eventDate.toString(),currentTripId);
        try
        {
            addTripEvent = TripDatabase.getInstance(null).eventDAO().insert(newEvent);
            if (addTripEvent >= 0) {
                Toast.makeText(EventFormActivity.this, "Event Added", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
            finish();
        }
        catch(Exception e)
        {
            Toast.makeText(EventFormActivity.this, "Could Not Add Event", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}