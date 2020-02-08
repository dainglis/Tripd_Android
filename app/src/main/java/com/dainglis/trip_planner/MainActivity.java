package com.dainglis.trip_planner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton AddBtnMove;

    static final String KEY_TRIP_ID = "tripId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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



    // Method       :   launchTripFormForResult()
    // Description  :
    //                  This method is called when the add button is pressed.
    //                  when pressed set up page is called.
    // Parameter    :   None
    // Returns      :   Void

    public void launchTripFormForResult() {
        Intent intent = new Intent(this, ActivitySetupPage.class);
        startActivityForResult(intent, ActivityRequest.TRIP_FORM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityRequest.TRIP_FORM) {
            if (resultCode == RESULT_OK) {
                // refresh the event list
                Toast.makeText(getApplicationContext(), "New Trip Created", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Trip Creation Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void setTripListView() {
        TripDatabase db = TripDatabase.getInstance(getApplicationContext());

        final List<Trip> trips = db.tripDAO().getAll();
        ArrayList<String> tripNames = new ArrayList<>();

        for (int i = 0; i < trips.size(); i++) {
            tripNames.add(trips.get(i).getTitle());
        }

        // This is a very simplified listview display of a list of content
        // using the built-in android.R.layout.simple_list_item_1
        // We will need to create a custom ListAdapter to support the Trip data objects
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tripNames);

        ListView tripList = findViewById(R.id.tripCardListView);
        tripList.setAdapter(itemsAdapter);

        tripList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), ActivityInfoPage.class);
                intent.putExtra(KEY_TRIP_ID, trips.get(position).getId());
                startActivity(intent);
            }
        });
    }


}
