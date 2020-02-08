package com.dainglis.trip_planner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivitySetupPage extends AppCompatActivity {

    // Create vars for cancel and confirm buttons
    Button CanButt;
    Button BtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create action for cancel button
        CanButt = findViewById(R.id.buttonSetupCancel);

        //create addBtn listener
        CanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelForm();
                //openActivity();
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


    // Method       :   dateValidate()
    // Description  :
    //                  This method is called when the add button to validate
    //                  date input entered by user
    // Parameter    :   dateString
    // Returns      :   bool

    public boolean dateValidate(String dateString)
    {
        // set the date format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date ObjDate = null;
        df.setLenient(false);

        // use try catch to compare string with df format
        try
        {

            ObjDate = df.parse(dateString);
            return true;
        }
        catch(Exception e)
        {
            return false;

        }
    }

}

