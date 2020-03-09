
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       MainActivity.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    The main activity is the landing page of the Tripd app. This page contains the
                    code behind to display trip "cards" and add new trips.

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.ActivityRequest;
import com.dainglis.trip_planner.controllers.CustomArrayAdapter;
import com.dainglis.trip_planner.models.Trip;
import com.dainglis.trip_planner.controllers.TripDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton AddBtnMove;

    static final String KEY_TRIP_ID = "tripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // On creation of the MainActivity, the initial Fragment
        // TripListFragment is launched

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create action for add button
        AddBtnMove = findViewById(R.id.addBtn);

        //create addBtn listener
        AddBtnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchTripFormForResult();
            }
        });

        /*
            This is a test to ensure that the TripDatabase initializes correctly
         */
        AsyncTask.execute(new Runnable() {
           @Override
           public void run() {
                TripDatabase.init(getApplicationContext());
                setTripListView();
           }
        });

    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         onCreateOptionsMenu()      -- Override
        Description:    This method creates the options menu and allows it to inflate.
        Parameters:     Menu        menu        The options menu
        Returns:        boolean     true

    --------------------------------------------------------------------------------------------- */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }


    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:         onOptionsItemSelected()      -- Override
        Description:    This method handles action bar item clicks.
        Parameters:     MenuItem    item        The item selected
        Returns:        boolean     true        If id is action_edit_trip or action_add_event
                        boolean     return from Super with identical parameter.

    --------------------------------------------------------------------------------------------- */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            /*
            */
            return true;
        }

        if (id == R.id.action_about) {
            // Show DialogFragment about page
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutDialog() {
        AboutDialogFragment adf = AboutDialogFragment.newInstance();
        adf.show(getSupportFragmentManager(), "dialog_fragment_about");
    }


/* METHOD HEADER COMMENT ---------------------------------------------------------------------------

    Method:         launchTripFormForResult()
    Description:    This method is called when the add button is pressed.
                    On press, set up page is called.
    Parameters:     None.
    Returns:        Void.

------------------------------------------------------------------------------------------------- */
    public void launchTripFormForResult() {
        Intent intent = new Intent(this, TripFormActivity.class);
        startActivityForResult(intent, ActivityRequest.TRIP_FORM);
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

    }



/* METHOD HEADER COMMENT ---------------------------------------------------------------------------

    Method:         setTripListView()
    Description:    This method is called to set the list of trips using a custom array adapter.
    Parameters:     None.
    Returns:        Void.

------------------------------------------------------------------------------------------------- */
    public void setTripListView() {

        TripDatabase db = TripDatabase.getInstance(getApplicationContext());

        final List<Trip> trips = db.tripDAO().getAll();

        // "Card" Array Adapter
        CustomArrayAdapter adapt = new CustomArrayAdapter(this, R.layout.card_view_list_item, trips);
        ListView tripList = findViewById(R.id.tripCardListView);
        tripList.setAdapter(adapt);

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), TripInfoActivity.class);
                intent.putExtra(KEY_TRIP_ID, trips.get(position).getId());
                startActivity(intent);

            }
        });
    }
}
