package com.dainglis.trip_planner;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ActivitySetupPage extends AppCompatActivity {

    // create var for cancel button
    Button CanButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_page2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // create action for cancel button
        CanButt = findViewById(R.id.buttonSetupCancel);

        //create addBtn listener
        CanButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });



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


}

