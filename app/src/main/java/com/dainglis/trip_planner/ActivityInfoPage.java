package com.dainglis.trip_planner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Event;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfoPage extends AppCompatActivity {

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
            final long tripId = extras.getLong("tripId");

            // Populate trip from id
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Trip currentTrip = getCurrentTrip(tripId);

                    if (currentTrip == null) {
                        System.out.println("No trip returned with id " + tripId);
                    }
                    else {
                        ArrayList<String> eventNames = getEventNames(tripId);


                        // Text views to be populated
                        TextView tripNameView = findViewById(R.id.tripName);
                        TextView startLocationView = findViewById(R.id.tripStart);
                        TextView endLocationView = findViewById(R.id.tripEnd);

                        tripNameView.setText(currentTrip.title);
                        startLocationView.setText(currentTrip.startLocation);
                        endLocationView.setText(currentTrip.endLocation);

                        // This is a very simplified listview display of a list of content
                        // using the built-in android.R.layout.simple_list_item_1
                        // We will need to create a custom ListAdapter to support the Trip data objects
                        ArrayAdapter<String> itemsAdapter =
                                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, eventNames);

                        ListView eventList = findViewById(R.id.eventListView);
                        eventList.setAdapter(itemsAdapter);

                    }

                }
            });
        }
    }

    private Trip getCurrentTrip(long id) {
        return TripDatabase.getInstance(getApplicationContext()).tripDAO().getById(id);
    }

    private List<Event> getEvents(long id) {
        return TripDatabase.getInstance(getApplicationContext()).eventDAO().getAllByTripId(id);
    }

    // TODO remove this, for testing purposes only
    private ArrayList<String> getEventNames(long id) {
        List<Event> events = getEvents(id);
        ArrayList<String> eventNames = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            eventNames.add(events.get(i).title);
        }

        return eventNames;
    }
}
