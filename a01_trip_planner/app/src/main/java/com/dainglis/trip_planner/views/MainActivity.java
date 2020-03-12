
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripDatabase;


public class MainActivity extends AppCompatActivity implements
        TripListFragment.OnFragmentInteractionListener,
        TripFormFragment.OnFragmentInteractionListener {

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
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase.init(getApplicationContext());
                TripDatabase.initializeCities();
                TripDatabase.loadSampleDataFromFile(getApplicationContext(), R.raw.test_data);
                //TripDatabase.loadSampleData();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        setInitialFragment(TripListFragment.newInstance());
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();
    }


    /*
     *  Replaces the current fragment in the FragmentManager with the specified fragment,
     *  adding the current fragment to the backstack
     */
    private void setActiveFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, "newFragment")
                //.setTransition(android.R.animator.fade_out)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
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


    /*
     *  Launches an AboutDialogFragment over the currently running Activity
     */
    private void showAboutDialog() {
        AboutDialogFragment adf = AboutDialogFragment.newInstance();
        adf.show(getSupportFragmentManager(), "dialog_fragment_about");
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

        return super.onOptionsItemSelected(item);
    }


    /*
     *  Interface methods from TripListFragment.OnFragmentInteractionListener
     */
    @Override
    public void onTripSelected(final long tripId) {
        //Toast.makeText(this, "Launching trip info page for id " + tripId, Toast.LENGTH_SHORT).show();

        TripInfoFragment infoFragment = TripInfoFragment.newInstance();
        infoFragment.setCurrentTripId(tripId);
        setActiveFragment(infoFragment);
        // This is a sample proof-of-concept for opening a Wikipedia link to the selected Trip's destination city

        /*
        TripDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Trip current = TripDatabase.getInstance().tripDAO().getByIdStatic(tripId);

                if (current != null) {
                    TripInfoFragment tripInfo = TripInfoFragment.newInstance();
                    tripInfo.setCurrentTripId(tripId);
                    setActiveFragment(tripInfo);

                    /*    String url = "https://www.wikipedia.org/wiki/" + current.getEndLocation();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);

                }
            }
        });

         */

    }

    @Override
    public void onAddButtonPressed() {
        setActiveFragment(TripFormFragment.newInstance());
        //
        //Toast.makeText(this, "Launching fragment to create new Trip", Toast.LENGTH_SHORT).show();
    }


    /*
     *  Interface methods for TripFormFragment
     */
    @Override
    public void onTerminateTripForm() {
        previousFragment();
    }



    /*
    *   DEPREC
    *   this is only appropriate procedure for a DialogFragment, as it is shown
    *   on top of the current window
    private void showFormFrag() {

        TripFormFragment tff = TripFormFragment.newInstance();
        tff.show(getSupportFragmentManager(),"fragment_trip");
    }
     */
    
}
