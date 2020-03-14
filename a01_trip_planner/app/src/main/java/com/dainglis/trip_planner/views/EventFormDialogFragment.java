
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       EventFormDialogFragment.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    This file contains the definition of the EventFormDialogFragment class.
                    An object of this class has a Trip, event title, event date, and allows the
                    user to edit, or save an event.

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
*   Class:          EventFormDialogFragment
*
*   Members:
*   private:
*
*   Methods:
*   private:
*
*/
public class EventFormDialogFragment extends DialogFragment {

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

    ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Get trip data members from database
        //currentTrip = getCurrentTrip(currentTripId);

        //Connect title and date variables to EditText fields
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
                //validate non-empty title and valid date within trip date range
                if (validateEventForm()) {
                    saveEventForm();
                }
            }
        });

        //Get tripId passed when Add Event Activity invoked
        extras = getIntent().getExtras();

        if (extras != null) {
            currentTripId = extras.getLong("tripId");

            if (currentTripId == 0) {
                cancelForm();
            }
        }

    }
    */



    public EventFormDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutDialogFragment.
     */
    public static EventFormDialogFragment newInstance() {
        EventFormDialogFragment fragment = new EventFormDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_event_form, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onDetach() {
        super.onDetach();
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



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         cancelForm()
        Description:    Cancels the form for creating a new Trip, telling the calling activity
                        that it was cancelled.
        Parameters:     void
        Returns:        void;       Sets result to RESULT_CANCELED

    --------------------------------------------------------------------------------------------- */
    private void cancelForm() {

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
            Toast.makeText(getContext(),
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
            Toast.makeText(getContext(),
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

            //setResult(RESULT_OK);
            //finish();
        }
        catch(Exception e)
        {
            Toast.makeText(getContext(), "Error adding event", Toast.LENGTH_SHORT).show();
            //setResult(RESULT_CANCELED);

        }
    }
}
