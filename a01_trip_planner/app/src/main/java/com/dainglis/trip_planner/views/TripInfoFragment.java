package com.dainglis.trip_planner.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.controllers.TripInfoViewModel;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.models.Trip;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripInfoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TripInfoViewModel mViewModel;

    View view;
    TextView tripNameView;
    TextView startLocationView;
    TextView endLocationView;
    TextView tripDateView;
    FloatingActionButton addButton;
    FloatingActionButton editButton;

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
        view = inflater.inflate(R.layout.fragment_trip_info, container, false);

        tripNameView = view.findViewById(R.id.tripName);
        startLocationView = view.findViewById(R.id.tripStart);
        endLocationView = view.findViewById(R.id.tripEnd);
        tripDateView = view.findViewById(R.id.tripDateInfo);

        addButton = view.findViewById(R.id.buttonAdd);
        editButton = view.findViewById(R.id.buttonEdit);

        if (currentTripId == 0) {
            Toast.makeText(view.getContext(),"No tripId received", Toast.LENGTH_SHORT)
                    .show();
        }
        return view;
    }


    /*
     * Add Listeners and Observers to data objects when the Fragment is fully instantiated
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TripInfoViewModel.class);
        mViewModel.setTripId(currentTripId);

        mViewModel.getTrip().observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                updateTripView(trip);
            }
        });

        mViewModel.getEvents().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {
                updateEventView(events);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseAddButtonPressed(mViewModel.getTripId());
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raiseEditButtonPressed(mViewModel.getTripId());
            }
        });
    }

    private void updateTripView(@Nullable Trip trip) {
        if (trip != null) {
            tripNameView.setText(trip.getTitle());
            startLocationView.setText(trip.getStartLocation());
            endLocationView.setText(trip.getEndLocation());
            tripDateView.setText(trip.getDateStamp());
        }
    }

    private void updateEventView(@Nullable List<Event> events) {
        if (events != null && events.size() > 0) {
            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

            SimpleAdapter eventsAdapter = new SimpleAdapter(view.getContext(),
                    mViewModel.generateEventInfoList(events),
                    R.layout.two_line_list_item,
                    new String[] {TripInfoViewModel.KEY_MAIN_TEXT, TripInfoViewModel.KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = view.findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TripInfoFragment.OnFragmentInteractionListener) {
            mListener = (TripInfoFragment.OnFragmentInteractionListener) context;
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

    private void raiseAddButtonPressed(long tripId) {
        mListener.onAddButtonPressed(tripId);
    }

    private void raiseEditButtonPressed(long tripId) {
        mListener.onEditButtonPressed(tripId);
    }


    public interface OnFragmentInteractionListener {
        //void interfaceFunction();
        void onAddButtonPressed(long tripId);
        void onEditButtonPressed(long tripId);
    }

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

       Method:         displayTripDetailsAsync()
       Description:    This method executes the displayTripDetails method as an asynchronous task.
       Parameters:     final long  tripId      The trip for which details are required.
       Returns:        Void.

   --------------------------------------------------------------------------------------------- */
    /*
    protected void displayTripDetailsAsync(final long tripId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                displayTripDetails(tripId);
            }
        });
    }

     */



    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         displayTripDetails()
        Description:    Given a unique id for a Trip, the details of the Trip are retrieved from
                        the "trip_planner" database.
        Parameters:     long        tripId      The id of the Trip to query the database for and
                                                populate the layout elements with the Trip data.
        Returns:        Void.

    --------------------------------------------------------------------------------------------- */
    /*
    protected void displayTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId);

        if (currentTrip != null) {
            // Text views to be populated


            tripNameView.setText(currentTrip.getTitle());
            startLocationView.setText(currentTrip.getStartLocation());
            endLocationView.setText(currentTrip.getEndLocation());
            tripDateView.setText(currentTrip.getDateStamp());


            // A simplified two-line list item using the
            // two_line_list_item.xml layout file

            SimpleAdapter eventsAdapter = new SimpleAdapter(getActivity().getApplicationContext(),
                    generateEventInfoList(tripId),
                    R.layout.two_line_list_item,
                    new String[] {KEY_MAIN_TEXT, KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = view.findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);

        }
    }
     */

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------
        Method:         getCurrentTrip()
        Description:    Queries the `trip_planner` database for the Trip object with the specified
                        id.
        Parameters:     long        id      The unique id of the requested Trip object.
        Returns:        Trip                The Trip object corresponding to the provided Id
    ---------------------------------------------------------------------------------------------
    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance().tripDAO().getByIdStatic(id);
    }

     */


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         getEvents()
        Description:    Queries the "trip_planner" database for the Trip object with the specified
                        id.
        Parameters:     long        id          The id of the Trip to query the database for.
        Returns:        List<Event>             The events in the specified Trip.

    --------------------------------------------------------------------------------------------- */

    /*
    private List<Event> getEvents(long tripId) {
        return TripDatabase.getInstance().eventDAO().getAllByTripId(tripId);
    }

     */

}
