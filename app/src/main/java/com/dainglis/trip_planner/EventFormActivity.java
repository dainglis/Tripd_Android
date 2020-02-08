package com.dainglis.trip_planner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EventFormActivity extends AppCompatActivity {

    Button btnCancel;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Set click listener for Cancel button
        btnCancel = findViewById(R.id.buttonEventFormCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelForm();
            }
        });


        // Set click listener for Confirm button
        btnConfirm = findViewById(R.id.buttonEventFormConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEventForm()) {
                    saveEventForm();
                }
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


    /*
     *  Method      : validateEventForm
     *  Description :
     *      Validates the EditText fields of the form's layout, ensuring
     *      the "Title" is a valid string, and the "DateTime" is in
     *      the correct DateTime format: YYYY-MM-DD HH:MM:SS
     *
     *  Parameters  :
     *      void
     *  Returns     :
     *      boolean : `true` if the form is valid, `false` otherwise
     */
    private boolean validateEventForm() {
        Toast.makeText(getApplicationContext(), "This is where form validation goes", Toast.LENGTH_SHORT).show();

        return false;
    }


    /*
     *  Method      : saveEventForm
     *  Description :
     *      Attempts to save the form as a new Event in the database
     *
     *  Parameters  :
     *      void
     *  Returns     :
     *      void
     */
    private void saveEventForm() {

    }

}
