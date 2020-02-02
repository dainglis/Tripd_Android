package com.dainglis.trip_planner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;        // For LogCat debug messages
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.dainglis.trip_planner.data.TripDatabase;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton AddBtnMove;

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
        /*

        We will use this FAB for the "Add Trip" button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
        */
          
        /*
            This is a test to ensure that the TripDatabase inits correctly
         */
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase db = TripDatabase.getInstance(getApplicationContext());

                TripDatabase.loadSampleData();

                Log.d(null, "There are " + db.tripDAO().getAll().size() + " trips");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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





}
