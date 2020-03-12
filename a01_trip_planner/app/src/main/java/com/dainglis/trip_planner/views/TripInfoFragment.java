package com.dainglis.trip_planner.views;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.controllers.TripDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripInfoFragment extends Fragment {

    private static final String KEY_MAIN_TEXT = "main";
    private static final String KEY_SECONDARY_TEXT = "secondary";

    View view;
    TextView tripNameView;
    TextView startLocationView;
    TextView endLocationView;
    TextView tripDateView;

    public long currentTripId;
    //Bundle extras;

    public TripInfoFragment() {
        // Required empty public constructor
    }

    public void setCurrentTripId(long id){
        currentTripId = id;
    }
    public static TripInfoFragment newInstance() {


        TripInfoFragment fragment = new TripInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_trip_form, container, false);


        if (currentTripId == 0) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "No extras received",
                    Toast.LENGTH_SHORT).show();
        }
        else {

            Toast.makeText(getActivity().getApplicationContext(),
                    "Extra: " + currentTripId,
                    Toast.LENGTH_SHORT).show();
            displayTripDetailsAsync(currentTripId);
        }
        return view;
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

       Method:         displayTripDetailsAsync()
       Description:    This method executes the displayTripDetails method as an asynchronous task.
       Parameters:     final long  tripId      The trip for which details are required.
       Returns:        Void.

   --------------------------------------------------------------------------------------------- */
    protected void displayTripDetailsAsync(final long tripId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                displayTripDetails(tripId);
            }
        });
    }



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         displayTripDetails()
        Description:    Given a unique id for a Trip, the details of the Trip are retrieved from
                        the "trip_planner" database.
        Parameters:     long        tripId      The id of the Trip to query the database for and
                                                populate the layout elements with the Trip data.
        Returns:        Void.

    --------------------------------------------------------------------------------------------- */
    protected void displayTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId).getValue();

        if (currentTrip != null) {
            // Text views to be populated
            tripNameView = view.findViewById(R.id.tripName);
            startLocationView = view.findViewById(R.id.tripStart);
            endLocationView = view.findViewById(R.id.tripEnd);
            tripDateView = view.findViewById(R.id.tripDateInfo);

            tripNameView.setText(currentTrip.getTitle());
            startLocationView.setText(currentTrip.getStartLocation());
            endLocationView.setText(currentTrip.getEndLocation());
            tripDateView.setText(currentTrip.getDateStamp());


            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

/*            SimpleAdapter eventsAdapter = new SimpleAdapter(this,
         AAAAAAAAAAAAAAAAAAAAAAAA           generateEventInfoList(tripId),
                    R.layout.two_line_list_item,
                    new String[] {KEY_MAIN_TEXT, KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);*/

        }
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         getCurrentTrip()
        Description:    Queries the `trip_planner` database for the Trip object with the specified
                        id.
        Parameters:     long        id      The unique id of the requested Trip object.
        Returns:        Trip                The Trip object corresponding to the provided Id
    --------------------------------------------------------------------------------------------- */
    private LiveData<Trip> getCurrentTrip(long id) {
        return TripDatabase.getInstance().tripDAO().getById(id);
    }
}
