package com.dainglis.trip_planner;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;        // For LogCat debug messages
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton AddBtnMove;

    TripDatabase tripDatabase;

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
                openActivity2();
            }
        });

          
        /*
            This is a test to ensure that the TripDatabase inits correctly
         */
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                initTripDatabase();

                refreshTripListView();
            }
        });


    }



    // Method       :   openActivity2()
    // Description  :
    //                  This method is called when the add button is pressed.
    //                  when pressed set up page is called.
    // Parameter    :   None
    // Returns      :   Void

    public void openActivity2() {
        Intent intent = new Intent(this, ActivitySetupPage.class);
        startActivity(intent);
    }

    // TODO move this to TripDatabase.java
    public void initTripDatabase() {
        TripDatabase db = TripDatabase.getInstance(getApplicationContext());

        System.out.println("There are " + db.tripDAO().getAll().size() + " trips");

        TripDatabase.loadSampleData();

        System.out.println("There are now " + db.tripDAO().getAll().size() + " trips");
    }

    public void refreshTripListView() {
        TripDatabase db = TripDatabase.getInstance(getApplicationContext());

        final List<Trip> trips = db.tripDAO().getAll();
        ArrayList<String> tripNames = new ArrayList<>();

        for (int i = 0; i < trips.size(); i++) {
            tripNames.add(trips.get(i).title);
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
                intent.putExtra("tripId", trips.get(position).getId());
                startActivity(intent);
            }
        });
    }


}
