
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
import android.support.v4.app.Fragment;
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


    static final String KEY_TRIP_ID = "tripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On creation of the MainActivity, the initial Fragment
        // TripListFragment is launched


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
            This is a test to ensure that the TripDatabase initializes correctly
         */
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase.init(getApplicationContext());
            }
        });

        TripListFragment tlFragment = TripListFragment.newInstance();
        setActiveFragment(tlFragment);

    }

    private void setActiveFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitNow();
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


    /*
     *
     */
    private void showAboutDialog() {
        AboutDialogFragment adf = AboutDialogFragment.newInstance();
        adf.show(getSupportFragmentManager(), "dialog_fragment_about");
    }




    /*
    *
    *
    */

    private void showFormFrag() {

        TripFormFragment tff = TripFormFragment.newInstance();
        tff.show(getSupportFragmentManager(),"fragment_trip");
    }
}
