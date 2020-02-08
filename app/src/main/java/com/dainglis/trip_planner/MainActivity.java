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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Event;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // "Card Adapter" ==========================================================================

        String[] tripName = {};
        String[] tripStartCity = {};
        String[] tripEndCity = {};
        String[] tripStartDate = {};
        String[] tripEndDate = {};

        for (int i = 0; i < trips.size(); i++) {
            tripName[i] = trips.get(i).getTitle();
            tripStartCity[i] = trips.get(i).getStartLocation();
            tripEndCity[i] = trips.get(i).getEndLocation();
            tripStartDate[i] = trips.get(i).getStartDate();
            tripEndDate[i] = trips.get(i).getEndDate();
        }

        CustomCardListAdapter cardAdapter = new CustomCardListAdapter(this, tripName, tripStartCity,
                tripEndCity, tripStartDate, tripEndDate);

        ListView tripList = findViewById(R.id.tripCardListView);
        tripList.setAdapter(cardAdapter);

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



