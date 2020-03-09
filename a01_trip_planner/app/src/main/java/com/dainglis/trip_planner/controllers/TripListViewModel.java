package com.dainglis.trip_planner.controllers;

import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.views.TripFormActivity;
import com.dainglis.trip_planner.views.TripInfoActivity;

import java.util.List;

public class TripListViewModel extends ViewModel {
    // TODO: Implement the ViewModel


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
