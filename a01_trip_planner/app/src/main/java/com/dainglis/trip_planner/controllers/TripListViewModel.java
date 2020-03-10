package com.dainglis.trip_planner.controllers;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.widget.ListView;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;

import java.util.List;

public class TripListViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private TripListAdapter mTripAdapter;

    private LiveData<List<Trip>> mTrips;

    public TripListViewModel() {
        //super(app);

        mTrips = TripDatabase.getInstance().tripDAO().getAll();

    }

    public LiveData<List<Trip>> getTrips() {
        return mTrips;
    }

    /*
    public void insertTrip(final Trip trip) {
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                mTripDAO.insert(trip);
            }
        });
    }*/


    /* METHOD HEADER COMMENT ---------------------------------------------------------------------------

        Method:         setTripListView()
        Description:    This method is called to set the list of trips using a custom array adapter.
        Parameters:     None.
        Returns:        Void.

    ------------------------------------------------------------------------------------------------- */
    public void setTripListView(ListView listView) {
        /*
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase db = TripDatabase.getInstance();
                final LiveData<List<Trip>> trips = db.tripDAO().getAll();

                // "Card" Array Adapter

                adapt = new TripListAdapter(context, R.layout.card_view_list_item, trips);
                tripList.setAdapter(adapt);
            }
        });
         */


        /* temporarily removed onclicklistener, this will need to raise an event in the implementing activity
        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), TripInfoActivity.class);
                intent.putExtra(KEY_TRIP_ID, trips.get(position).getId());
                startActivity(intent);

            }
        });
         */
    }

    /* METHOD HEADER COMMENT ---------------------------------------------------------------------------

        Method:         launchTripFormForResult()
        Description:    This method is called when the add button is pressed.
                        On press, set up page is called.
        Parameters:     None.
        Returns:        Void.

    ------------------------------------------------------------------------------------------------- */
    public void launchTripFormForResult() {
        /*
        Intent intent = new Intent(this, TripFormActivity.class);
        startActivityForResult(intent, ActivityRequest.TRIP_FORM);
        */
    }



    /* METHOD HEADER COMMENT ---------------------------------------------------------------------------

        Method:         onActivityResult()
        Description:    This method is called when an activity returns a result.
        Parameters:     int         requestCode         If the trip form activity has been requested
                        int         resultCode          The result from the activity
                        Intent      data
        Returns:        Void.

    ------------------------------------------------------------------------------------------------- */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if (requestCode == ActivityRequest.TRIP_FORM) {
            if (resultCode == RESULT_OK) {
                // refresh the event list
                Toast.makeText(getApplicationContext(), "New Trip Created", Toast.LENGTH_SHORT).show();
                this.recreate();
            }
            else {
                Toast.makeText(getApplicationContext(), "Trip Creation Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
        */
    }




}
