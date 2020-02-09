package com.dainglis.trip_planner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDAO;
import com.dainglis.trip_planner.data.TripDatabase;

import java.io.Serializable;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivitySetupPage extends AppCompatActivity {

    // Create vars for cancel and confirm buttons
    Button CanButt;
    Button BtnConfirm;
    EditText EditName;
    EditText EditStartCity;
    EditText EditEndCity;
    EditText DateDepartEnter;
    EditText DateArriveEnter;
    public long currentTripId = 0;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditName = findViewById(R.id.editName);
        EditStartCity = findViewById(R.id.editStartCity);
        EditEndCity = findViewById(R.id.editEndCity);
        DateDepartEnter = findViewById(R.id.dateDepartEnter);
        DateArriveEnter = findViewById(R.id.dateArriveEnter);

        CanButt = findViewById(R.id.buttonSetupCancel);         // create action for cancel button
        CanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //create addBtn listener
                cancelForm();
                //openActivity();
            }
        });
        BtnConfirm = findViewById(R.id.buttonSetupConfirm); // create action for confirm button
        BtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//create addBtn listener
                if( !fieldValidation(EditName) || !fieldValidation(EditStartCity) || !fieldValidation(EditEndCity)||
                        !dateValidate(DateDepartEnter) || !dateValidate(DateArriveEnter) )
                {
                }else
                submitForm();
            }
        });

        extras = getIntent().getExtras();

        if (extras == null) {
        }
        else {

            currentTripId = extras.getLong("tripId");

            if (currentTripId != 0) {

                // Query the database and update the layout in a secondary thread
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        loadTripDetails(currentTripId);
                    }
                });
            }
        }

    }

    /*
     *  Method      : cancelForm
     *  Description :
     *      Cancels the form for creating a new Trip, telling
     *      the calling activity that it was cancelled.
     *
     *  Parameters  :
     *      void
     *  Returns     :
     *      void
     */
    private void cancelForm() {
        setResult(RESULT_CANCELED);
        finish();
    }


    // Method       :   openActivity()
    // Description  :
    //                  This method is called when the add button is pressed.
    //                  when pressed main page page is called.
    // Parameter    :   None
    // Returns      :   Void

    public void openActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /*
     *  Method      : cancelForm
     *  Description :
     *      Cancels the form for creating a new Trip, telling
     *      the calling activity that it was cancelled.
     *
     *  Parameters  :
     *      void
     *  Returns     :
     *      void
     */
    private void submitForm() {
        String title = EditName.getText().toString();
        String startLocation = EditStartCity.getText().toString();
        String endLocation = EditEndCity.getText().toString();
        String startDate = DateDepartEnter.getText().toString();
        String endDate = DateArriveEnter.getText().toString();

        final Trip trip = new Trip(title, startLocation, endLocation, startDate, endDate);

        try {
            if (extras == null) {
                // Save the new trip to the db in a background thread,
                // then return to the calling Activity
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        TripDatabase.getInstance(null).tripDAO().insert(trip);
                        //      Toast.makeText(ActivitySetupPage.this,"Trip saved", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                trip.setTripId(currentTripId);
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        TripDatabase.getInstance(null).tripDAO().update(trip);
                        //     Toast.makeText(ActivitySetupPage.this,"Trip edited", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            setResult(RESULT_OK);
        }
        // this is bad practice
        catch (Exception e) {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    // Method       :   dateValidate()
    // Description  :
    //                  This method is called when the add button to validate
    //                  date input entered by user
    // Parameter    :   dateString
    // Returns      :   bool

    public boolean dateValidate(EditText EditTextDate)
    {
        // set the date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date ObjDate = null;
        df.setLenient(false);

        // use try catch to compare string with df format
        try
        {

            ObjDate = df.parse(EditTextDate.getText().toString());
            return true;
        }
        catch(Exception e)
        {
            Toast.makeText(ActivitySetupPage.this,EditTextDate.getText().toString() + " is not a valid date.", Toast.LENGTH_LONG).show();
            return false;

        }
    }

    public boolean fieldValidation(EditText editText)
    {
        if (editText.getText().toString() == null || editText.getText().toString().trim().length() < 1){
            Toast.makeText(ActivitySetupPage.this,"Fill the complete trip information", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    protected void loadTripDetails(long tripId) {

        // Query the database for the Trip associated with tripId
        Trip currentTrip = getCurrentTrip(tripId);

        if (currentTrip != null) {
            // Edit texts to be populated

            EditName.setText(currentTrip.getTitle());
            EditStartCity.setText(currentTrip.getStartLocation());
            EditEndCity.setText(currentTrip.getEndLocation());
            DateDepartEnter.setText(currentTrip.getStartDate());
            DateArriveEnter.setText(currentTrip.getEndDate());

        }
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


}

