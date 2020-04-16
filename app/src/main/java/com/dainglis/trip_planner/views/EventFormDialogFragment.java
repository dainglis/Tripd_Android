
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

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.providers.TripDataContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
public class EventFormDialogFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    // Instance variable
    long currentTripId = 0;

    // View elements
    EditText eventTitle;

    Button btnCancel;
    Button btnConfirm;

    Button btnSelectTime;
    Button btnSelectDate;

    boolean formDateSet;
    boolean formTimeSet;
    boolean currentTripSet;



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

        formDateSet = false;
        formTimeSet = false;

        eventTitle = view.findViewById(R.id.eventTitleEdit);

        btnCancel = view.findViewById(R.id.buttonEventFormCancel);
        btnConfirm = view.findViewById(R.id.buttonEventFormConfirm);

        btnSelectDate = view.findViewById(R.id.eventDateSelect);
        btnSelectTime = view.findViewById(R.id.eventTimeSelect);


        // Set up on-click listeners for Confirm and Cancel buttons
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminateEventForm();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEventForm()) {
                    saveEventForm();
                    terminateEventForm();
                }
            }
        });

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDatePicker(view.getContext());
            }
        });

        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTimePicker(view.getContext());
            }
        });

        return view;
    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         launchDatePicker()
        Description:    Launches a DatePickerDialog, whose result is captured by the OnDateSetListener
        Parameters:     Context context - The current Context
        Returns:        void

    --------------------------------------------------------------------------------------------- */
    public void launchDatePicker(Context context) {
        new DatePickerDialog(context, 0, this, 2020, 1,1).show();
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         launchTimePicker()
        Description:    Launches a TimePickerDialog, whose result is captured by the OnTimeSetListener
        Parameters:     Context context - The current Context
        Returns:        void

    --------------------------------------------------------------------------------------------- */
    public void launchTimePicker(Context context) {
        new TimePickerDialog(context, this, 0, 0, true).show();
    }


    public void setCurrentTripId(long tripId) {
        currentTripId = tripId;
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

        Method:         terminateEventForm()
        Description:    Closes the form for creating a new Event
        Parameters:     void
        Returns:        void

    --------------------------------------------------------------------------------------------- */
    private void terminateEventForm() {
        dismiss();

    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         validateEventForm()
        Description:    Validates the EditText fields of the form's layout, ensuring the "Title" is
                        a valid string.
                        Checks to see if the Date and Time fields of the form have been set by their
                        respective Dialog Pickers. Validation of the dates is performed in the listener
        Parameters:     void
        Returns:        boolean         True            If the form is valid
                                        False           If the form is invalid.

    --------------------------------------------------------------------------------------------- */
    private boolean validateEventForm() {
        boolean isValid = false;
        String toastData = "";

        if (eventTitle.getText().toString().trim().length() < 1) {
            toastData = "Event title cannot be blank";
        }
        else if (!formDateSet) {
            toastData = "Please select a date for the event";
        }
        else if (!formTimeSet) {
            toastData = "Please select a time for the event";
        }
        else {
            isValid = true;
        }

        if (!isValid) {
            Toast.makeText(getContext(),
                    toastData,
                    Toast.LENGTH_SHORT).show();
        }

        return isValid;
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
                        btnSelectDate.getText().toString() + " " + btnSelectTime.getText().toString(),
                        currentTripId);
        try {
            TripDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    TripDatabase.getInstance().eventDAO().insert(event);
                }
            });

        }
        catch(Exception e)
        {
            Toast.makeText(getContext(), "Error adding event", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onDateSet(DatePicker datePicker, int yr, int mo, int day) {
        DateFormat eventDateFormat = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);
        StringBuilder eventDate = new StringBuilder();
        eventDate.append(yr );
        eventDate.append("-");
        eventDate.append( (mo < 10) ? "0" + mo : mo);
        eventDate.append("-");
        eventDate.append( (day < 10) ? "0" + day : day);


        try {
            eventDateFormat.parse(eventDate.toString());
            formDateSet = true;
            btnSelectDate.setText(eventDate.toString());
            Log.d("DatePicker", "Date has been set: " + eventDate.toString());
        }
        catch (ParseException exc) {
            Log.e("DatePicker", "Malformed date from picker");
            formDateSet = false;
        }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hr, int min) {
        DateFormat eventTimeFormat = new SimpleDateFormat(TripDataContract.TIME_FORMAT, Locale.CANADA);
        StringBuilder eventTime = new StringBuilder();
        eventTime.append( (hr < 10) ? "0" + hr : hr);
        eventTime.append(":");
        eventTime.append( (min < 10) ? "0" + min : min);

        try {
            eventTimeFormat.parse(eventTime.toString());
            formTimeSet = true;
            btnSelectTime.setText(eventTime.toString());
            Log.d("DatePicker", "Time has been set: " + eventTime.toString());
        }
        catch (ParseException exc) {
            Log.e("DatePicker", "Malformed time from picker");
            formTimeSet = false;
        }
    }
}
