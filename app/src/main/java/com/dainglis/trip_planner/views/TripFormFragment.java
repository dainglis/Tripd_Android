
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

import android.app.DatePickerDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.CityRepo;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.providers.TripDataContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripFormFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final int DATE_PICKER_DEPART = 0;
    private static final int DATE_PICKER_ARRIVE = 1;

    private long currentTripId = 0;

    DateFormat tripDateFormat;

    // View elements
    Button CanButt;
    Button BtnConfirm;
    Spinner CityStartSpinner;
    Spinner CityEndSpinner;
    EditText EditName;
    Button DateDepartSelect;
    Button DateArriveSelect;

    // Pointer for active elements
    Button activeDateSelection;

    LiveData<Trip> mTrip;

    private OnFragmentInteractionListener mListener;

    public TripFormFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutDialogFragment.
     */
    public static TripFormFragment newInstance() {
        TripFormFragment fragment = new TripFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_form, container, false);

        activeDateSelection = null;
        tripDateFormat = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);

        EditName = view.findViewById(R.id.editName);
        DateDepartSelect = view.findViewById(R.id.dateDepartSelect);
        DateArriveSelect = view.findViewById(R.id.dateArriveSelect);
        CityStartSpinner = view.findViewById(R.id.StartSpinner);
        CityEndSpinner = view.findViewById(R.id.EndSpinner);

        populateSpinners();

        // create action for cancel button
        CanButt = view.findViewById(R.id.buttonSetupCancel);
        CanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create addBtn listener
                cancelForm();
            }
        });


        BtnConfirm = view.findViewById(R.id.buttonSetupConfirm); // create action for confirm button
        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create addBtn listener
                // all are true then submit
                if (fieldValidation("Trip Name", EditName.getText().toString())
                        && spinnerValidation("Start City", CityStartSpinner.getSelectedItemPosition())
                        && spinnerValidation("End City", CityEndSpinner.getSelectedItemPosition())
                        && selectedDatesValidation(DateDepartSelect.getText().toString(),
                                DateArriveSelect.getText().toString()) ) {
                    submitForm();
                }

            }

        });


        DateDepartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDatePicker(view.getContext(), DATE_PICKER_DEPART);
            }
        });

        DateArriveSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDatePicker(view.getContext(), DATE_PICKER_ARRIVE);
            }
        });

        mTrip = TripDatabase.getInstance().tripDAO().getById(currentTripId);
        mTrip.observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                updateTripDetails(trip);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripFormFragment.OnFragmentInteractionListener) {
            mListener = (TripFormFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         cancelForm()
        Description:    Cancels the form for creating a new Trip, telling the calling activity that
                        it was cancelled.
        Parameters:     void.
        Returns:        void.
    --------------------------------------------------------------------------------------------- */
    private void cancelForm() {
        mListener.onTerminateTripForm();
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
        String startLocation = CityStartSpinner.getSelectedItem().toString();
        String endLocation = CityEndSpinner.getSelectedItem().toString();
        String startDate = DateDepartSelect.getText().toString();
        String endDate = DateArriveSelect.getText().toString();

        final Trip trip = new Trip(title, startLocation, endLocation, startDate, endDate);

        try {
            if (currentTripId == 0) {
                // Save the new trip to the db in a background thread,
                // then return to the calling Activity
                TripDatabase.databaseWriteExecutor.execute(new Runnable() {

                    @Override
                    public void run() {
                        TripDatabase.getInstance().tripDAO().insert(trip);
                    }

                });
            }
            else {
                // Save the changes to the existing trip
                trip.setTripId(currentTripId);
                TripDatabase.databaseWriteExecutor.execute(new Runnable() {

                    @Override
                    public void run() {
                        TripDatabase.getInstance().tripDAO().update(trip);
                    }

                });
            }
        }

        // this is bad practice
        catch (Exception e) {
            Toast.makeText(getContext(), "Error writing to TripDatabase", Toast.LENGTH_SHORT)
                    .show();
        }

        mListener.onTerminateTripForm();
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         dateValidate()
        Description:    This method is called when the add button is triggered. Validating the
                        date entered by the user.
        Parameters:     EditText        EditTextDate        Date string.
        Returns:        boolean         True                If there are no exceptions thrown
                                        False               If an exception is caught.
    --------------------------------------------------------------------------------------------- */
    public boolean selectedDatesValidation(String departDateString, String arriveDateString) {
        // String.compareTo() allows the date strings to be compared
        // This comparison asserts that the arrival date must be "greater than
        // or equal to" the departure date, which indicates that the arrival date is
        // on or after the departure date

        if (departDateString.trim().length() < 1) {
            Toast.makeText(getActivity(), "Please select a departure date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (arriveDateString.trim().length() < 1) {
            Toast.makeText(getActivity(), "Please select an arrival date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (arriveDateString.compareTo(departDateString) < 0) {
            Toast.makeText(getActivity(), "Arrival date cannot be before departure date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         fieldValidation()
        Description:    This method is called when the add / edit button is pressed. This
                        method validates the input fields as entered by the user
        Parameters:     String      field
                            The name of the editable field
                        String      value
                            The String value in the selected field
        Returns:        boolean
                            true    If the field is non-empty
                            false   Otherwise
    --------------------------------------------------------------------------------------------- */
    public boolean fieldValidation(String field, String value) {
        if (value.trim().length() < 1) {
            String fieldToast = "\"" + field + "\" cannot be empty";
            Toast.makeText(this.getContext(), fieldToast, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         spinnerValidation()
        Description:    This method is called when the add / edit button is pressed. This
                        method validates the input fields as entered by the user
        Parameters:     String      field
                            The name of the editable field
                        int         selection
                            The String value in the selected field
        Returns:        boolean
                            true    If a valid City is selected
                            false   Otherwise
    --------------------------------------------------------------------------------------------- */
    public boolean spinnerValidation(String field, int selection) {
        if (selection == 0) {
            String fieldToast = "Select an option for \"" + field + "\"";
            Toast.makeText(this.getContext(), fieldToast, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         populateSpinners()
        Description:    Creates and populates the appropriate ArrayAdapter of City names for the
                        StartCity and EndCity Spinner widgets
        Parameters:     void
        Returns:        void
    --------------------------------------------------------------------------------------------- */
    private void populateSpinners() {
        List<String> cities = new ArrayList<>();

        cities.add("(Select City)");
        for (int i = 0; i< CityRepo.size(); i++) {
            cities.add(CityRepo.getName(i));
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner1
        CityStartSpinner.setAdapter(spinnerAdapter);

        // attaching data adapter to spinner2
        CityEndSpinner.setAdapter(spinnerAdapter);
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         loadTripDetails()
        Description:    Given a Trip from the "trip_planner" database, the View elements are updated
        Parameters:     Trip trip
                            The unique id of the requested Trip object.
        Returns:        void.
    --------------------------------------------------------------------------------------------- */
    private void updateTripDetails(@Nullable Trip trip) {
        if (trip != null) {
            EditName.setText(trip.getTitle());

            CityStartSpinner.setSelection(getCityIndex(CityStartSpinner, trip.getStartLocation()));
            CityEndSpinner.setSelection(getCityIndex(CityEndSpinner, trip.getEndLocation()));

            DateDepartSelect.setText(trip.getStartDate());
            DateArriveSelect.setText(trip.getEndDate());
        }
    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         getCityIndex()
        Description:    Returns the index in the Spinner's Array adapter at which the 'city' string
                        can be found
        Parameters:     Spinner spinner
                        String city
        Returns:        int
                            Index in the specified Spinner's ArrayAdapter at which the 'city'
                            string can be found
    --------------------------------------------------------------------------------------------- */
    private int getCityIndex(Spinner spinner, String city) {
        SpinnerAdapter adapter = spinner.getAdapter();
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (city.equals(adapter.getItem(i))) {
                    return i;
                }
            }
        }

        return 0;
    }


    public void setTripId(long tripId) {
        currentTripId = tripId;
    }


    public void launchDatePicker(Context context, int resource) {
        Calendar localCal = Calendar.getInstance();

        if (resource == DATE_PICKER_ARRIVE) {
            activeDateSelection = DateArriveSelect;
        }
        else if (resource == DATE_PICKER_DEPART) {
            activeDateSelection = DateDepartSelect;
        }
        else {
            return;
        }

        if (!activeDateSelection.getText().toString().equals("")) {
            try {
                localCal.setTime(tripDateFormat.parse(activeDateSelection.getText().toString()));
            } catch (ParseException exc) {
                return;
            }
        }

        new DatePickerDialog(context,
                0,
                this,
                localCal.get(Calendar.YEAR),
                localCal.get(Calendar.MONTH),
                localCal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int yr, int mo, int day) {
        if (activeDateSelection == null) {
            Log.e("DatePicker", "No active date selected");
            return;
        }

        ++mo; // For some reason, the DatePickerDialog Zero-indexes the month

        StringBuilder eventDate = new StringBuilder();
        eventDate.append(yr );
        eventDate.append("-");
        eventDate.append( (mo < 10) ? "0" + mo : mo);
        eventDate.append("-");
        eventDate.append( (day < 10) ? "0" + day : day);

        try {
            tripDateFormat.parse(eventDate.toString());
            activeDateSelection.setText(eventDate.toString());
            Log.d("DatePicker", "Date has been set: " + eventDate.toString());
        }
        catch (ParseException exc) {
            Log.e("DatePicker", "Malformed date from picker");;
        }
    }


    public interface OnFragmentInteractionListener {
        void onTerminateTripForm();
    }
}













