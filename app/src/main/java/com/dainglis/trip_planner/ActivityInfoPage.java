/*
    File    : ActivityInfoPage.java

        The InfoPage Activity presents information to the user about a
        specified Trip. The 'id' of the Trip must be passed to this
        Activity in the Intent as a `long` with key "tripId"

 */

package com.dainglis.trip_planner;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Event;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActivityInfoPage extends AppCompatActivity {

    private static final String KEY_MAIN_TEXT = "main";
    private static final String KEY_SECONDARY_TEXT = "secondary";



    public long currentTripId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("Starting InfoPage");

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Toast.makeText(getApplicationContext(),
                    "No extras received",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            currentTripId = extras.getLong("tripId");

            if (currentTripId != 0) {

                // Query the database and update the layout in a secondary thread
                displayTripDetailsAsync(currentTripId);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_trip) {
            // Selecting the "Edit Trip" menu option will launch the ActivitySetupPage
            // as an activity with a result, passing the member currentTripId
            Intent intent = new Intent(getApplicationContext(), ActivitySetupPage.class);
            intent.putExtra(MainActivity.KEY_TRIP_ID, currentTripId);
            //startActivity(intent);
            startActivityForResult(intent, ActivityRequest.TRIP_FORM);
            return true;
        }

        if (id == R.id.action_add_event) {
            // Selecting the "Edit Trip" menu option will launch the EventFormActivity
            // as an activity with a result, passing the member currentTripId
            Intent intent = new Intent(getApplicationContext(), EventFormActivity.class);
            intent.putExtra(MainActivity.KEY_TRIP_ID, currentTripId);
            //startActivity(intent);
            startActivityForResult(intent, ActivityRequest.EVENT_FORM);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
     *  Android Activity event invoked when an Activity started for Result
     *  returns to this Activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ActivityRequest.TRIP_FORM:

                if (resultCode == RESULT_OK) {
                    // refresh the form
                    Toast.makeText(getApplicationContext(), "Trip Details Edited", Toast.LENGTH_SHORT).show();
                    this.recreate();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Trip Details Unchanged", Toast.LENGTH_SHORT).show();
                }

                break;

            case ActivityRequest.EVENT_FORM:

                if (resultCode == RESULT_OK) {
                    // refresh the event list
                    Toast.makeText(getApplicationContext(), "New Event Created", Toast.LENGTH_SHORT).show();
                    this.recreate();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Event Creation Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    protected void displayTripDetailsAsync(final long tripId) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                displayTripDetails(tripId);
            }
        });
    }

    /*
     *  Method      : displayTripDetails
     *  Description :
     *      Given a unique id for a Trip, the details of the Trip are retrieved
     *      from the `trip_planner` database
     *  Parameters  :
     *      final long tripId : The id of the Trip to query the database for and
     *          populate the layout elements with the Trip data
     *  Returns     :
     *      void
     */
    protected void displayTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId);

        if (currentTrip != null) {
            // Text views to be populated
            TextView tripNameView = findViewById(R.id.tripName);
            TextView startLocationView = findViewById(R.id.tripStart);
            TextView endLocationView = findViewById(R.id.tripEnd);
            TextView tripDateView = findViewById(R.id.tripDateInfo);

            tripNameView.setText(currentTrip.getTitle());
            startLocationView.setText(currentTrip.getStartLocation());
            endLocationView.setText(currentTrip.getEndLocation());
            tripDateView.setText(currentTrip.getDateStamp());


            // A simplified two-line list item using the
            // two_line_list_item.xml layout file
            SimpleAdapter eventsAdapter = new SimpleAdapter(this,
                    generateEventInfoList(tripId),
                    R.layout.two_line_list_item,
                    new String[] {KEY_MAIN_TEXT, KEY_SECONDARY_TEXT },
                    new int[] {R.id.main_text, R.id.secondary_text });

            ListView eventList = findViewById(R.id.eventListView);
            eventList.setAdapter(eventsAdapter);

        }
    }


    /*
     *  Method      : generateEventInfoList
     *  Description :
     *      Queries the local database for all Events matching the specified
     *      tripId
     */
    private List<Map<String, String>> generateEventInfoList(long tripId) {
        List<Map<String, String>> data = new ArrayList<>();

        List<Event> events = getEvents(tripId);

        for (int i = 0; i < events.size(); i++) {
            Map<String, String> datum = new HashMap<>(2);
            datum.put(KEY_MAIN_TEXT, events.get(i).title);
            datum.put(KEY_SECONDARY_TEXT,events.get(i).date);
            data.add(datum);
        }

        return data;
    }



    /*
     *  Method      : getCurrentTrip
     *  Description :
     *      Queries the `trip_planner` database for the Trip object with the
     *      specified id
     *  Parameters  :
     *      long id : The unique id of the requested Trip
     *  Returns     :
     *      Trip : The Trip object with the corresponding id, or null
     */
    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance(getApplicationContext()).tripDAO().getById(id);
    }


    /*
     *  Method      : getCurrentTrip
     *  Description :
     *      Queries the `trip_planner` database for the Trip object with the
     *      specified id
     *  Parameters  :
     *      long id : The unique id of the requested Trip
     *  Returns     :
     *      Trip : The Trip object with the corresponding id, or null
     */
    private List<Event> getEvents(long tripId) {
        return TripDatabase.getInstance(getApplicationContext()).eventDAO().getAllByTripId(tripId);
    }
}
