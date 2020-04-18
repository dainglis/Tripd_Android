
/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       MainActivity.java
    PROJECT:        PROG3150 - Assignment 01
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           February 8th, 2020
    DESCRIPTION:    The MainActivity is the frame for each of the Fragment pages of the Tripd app.
                    It creates the AppBar with menu, and provides a container and interaction for
                    the Fragments

================================================================================================= */

package com.dainglis.trip_planner.views;

import android.content.ContentProvider;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.CityRepo;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.providers.TripDataContentProvider;
import com.dainglis.trip_planner.providers.TripDataContract;


public class MainActivity extends AppCompatActivity implements
        TripListFragment.OnFragmentInteractionListener,
        TripFormFragment.OnFragmentInteractionListener,
        TripInfoFragment.OnFragmentInteractionListener {

    static final long INITIAL_TRIP_NONE = 0;
    private long initialTripId = INITIAL_TRIP_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // On creation of the MainActivity, the initial Fragment
        // TripListFragment is launched

        Bundle applicationExtras = getIntent().getExtras();
        if (applicationExtras != null) {
            initialTripId = applicationExtras.getLong(TripDataContract.KEY_TRIP_ID);
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        View title = findViewById(R.id.toolbar_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clicking the app title returns to the main list Fragment
                setInitialFragment(TripListFragment.newInstance());
            }
        });
        /*
            This is a test to ensure that the TripDatabase initializes correctly
         */

        if (TripDatabase.getInstance() == null) {
            TripDatabase.databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("Tripd", "Initializing TripDB instance");
                    TripDatabase.init(getApplicationContext());
                    CityRepo.cities = TripDatabase.initializeCities();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        setInitialFragment(TripListFragment.newInstance());

        if (initialTripId != INITIAL_TRIP_NONE) {
            setActiveFragment(TripInfoFragment.newInstance(initialTripId));
        }
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


    /*
     * Replaces the placeholder fragment layout area with the main navigational fragment.
     * Prevents a blank fragment from appearing after the back button is pressed
     */
    private void setInitialFragment(Fragment fragment) {
        FragmentManager fManager = getSupportFragmentManager();
        while (fManager.getBackStackEntryCount() > 0) {
            fManager.popBackStackImmediate();
        }

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    /*
     *  Replaces the current fragment in the FragmentManager with the specified fragment,
     *  adding the current fragment to the backstack
     */
    private void setActiveFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    /*
     *  Pops the current Fragment off the stack of the FragmentManager, effectively
     *  returning to the previous Fragment on the stack
     */
    private void previousFragment() {
        getSupportFragmentManager().popBackStack();
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

        if (id == R.id.action_about) {
            // Show DialogFragment about page
            showAboutDialog();
            return true;
        }
        else if (id == R.id.action_test_data) {
            addAppTestData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void addAppTestData() {
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase.loadSampleDataFromFile(getApplicationContext(), R.raw.test_data);

                // Running tests on ContentProvider
                Uri uri = (new Uri.Builder()).scheme("content")
                        .authority(TripDataContract.AUTHORITY)
                        .appendPath("trips")
                        .build();

                Cursor c = getContentResolver().query(uri, null, null, null, null);
                if (c == null) {
                    Log.e("TripD_TEST", "Null cursor after query for all Trips");
                }
                else {
                    Log.d("TripD_TEST", "Obtained cursor of Trips");
                    while (c.moveToNext()) {
                        StringBuilder resBuilder = new StringBuilder();

                        for (int j = 0; j < c.getColumnCount(); j++) {
                            if (j != 0) {
                                resBuilder.append(", ");
                            }
                            resBuilder.append(c.getString(j));

                        }
                        Log.d("TripD_TEST", "Cursor provides Trip '" + resBuilder.toString());
                    }
                    c.close();
                }
            }
        });
    }

    /*
     *  Launches an AboutDialogFragment over the currently running Activity
     */
    private void showAboutDialog() {
        AboutDialogFragment adf = AboutDialogFragment.newInstance();
        adf.show(getSupportFragmentManager(), "dialog_fragment_about");
    }


    /*
     *  Interface methods from TripListFragment.OnFragmentInteractionListener
     */
    @Override
    public void onTripSelected(final long tripId) {
        //Creates a InfoFragment instance
        TripInfoFragment infoFragment = TripInfoFragment.newInstance(tripId);
        //Sends the tripId of the selected trip and also the Context
        infoFragment.setCurrentContext(MainActivity.this);
        //Change the Active Fragment
        setActiveFragment(infoFragment);
    }

    @Override
    public void onAddButtonPressed() {
        setActiveFragment(TripFormFragment.newInstance());
    }


    /*
     *  Interface methods for TripFormFragment
     */
    @Override
    public void onTerminateTripForm() {
        previousFragment();
    }


    /*
     *  Interface methods for TripInfoFragment
     */
    @Override
    public void onAddButtonPressed(long tripId) {
        EventFormDialogFragment eventForm = EventFormDialogFragment.newInstance();
        eventForm.setCurrentTripId(tripId);
        eventForm.show(getSupportFragmentManager(), "dialog_fragment_event_form");
    }

    @Override
    public void onEditButtonPressed(long tripId) {
        TripFormFragment formFragment = TripFormFragment.newInstance();
        formFragment.setTripId(tripId);

        setActiveFragment(formFragment);
    }

    //Method responsible for accessing Wikipedia pages of the cities
    public void onCityClick(View view){

        TextView tv = (TextView)view;
        //Sets the URL of the Wikipedia page we're going to visit
        String url = "https://www.wikipedia.org/wiki/" + tv.getText().toString() + ",_Ontario";
        //Creates the intent and start the Activity to reach the external site
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
