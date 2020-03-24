
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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.CityRepo;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.models.Trip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripFormFragment extends Fragment {

    private long currentTripId = 0;

    // View elements
    Button CanButt;
    Button BtnConfirm;
    Spinner CityStartSpinner;
    Spinner CityEndSpinner;
    EditText EditName;
    EditText DateDepartEnter;
    EditText DateArriveEnter;

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

        EditName = view.findViewById(R.id.editName);
        DateDepartEnter = view.findViewById(R.id.dateDepartEnter);
        DateArriveEnter = view.findViewById(R.id.dateArriveEnter);
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
                        && dateValidate(DateDepartEnter)
                        && dateValidate(DateArriveEnter) ) {
                    submitForm();
                }

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
        String startDate = DateDepartEnter.getText().toString();
        String endDate = DateArriveEnter.getText().toString();

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
            Toast.makeText(getActivity(),EditTextDate.getText().toString() + " is not a valid date.", Toast.LENGTH_LONG).show();
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
        if (editText.getText().toString().equals("") || editText.getText().toString().trim().length() < 1){

            Toast.makeText(getActivity(),"Fill the complete trip information", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
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

            DateDepartEnter.setText(trip.getStartDate());
            DateArriveEnter.setText(trip.getEndDate());
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


    public interface OnFragmentInteractionListener {
        void onTerminateTripForm();
    }
}













