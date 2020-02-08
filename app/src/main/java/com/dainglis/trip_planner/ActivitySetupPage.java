package com.dainglis.trip_planner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import com.dainglis.trip_planner.data.Trip;
import com.dainglis.trip_planner.data.TripDAO;
import com.dainglis.trip_planner.data.TripDatabase;

import java.util.List;

public class ActivitySetupPage extends AppCompatActivity {

    // Create vars for cancel and confirm buttons
    Button CanButt;
    Button BtnConfirm;
    EditText EditName;
    EditText EditStartCity;
    EditText EditEndCity;
    EditText DateDepartEnter;
    EditText DateArriveEnter;

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
                submitForm();

            }
        });
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

               AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                TripDatabase.getInstance(null).tripDAO().insert(trip);
                Toast.makeText(ActivitySetupPage.this,"Trip saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

