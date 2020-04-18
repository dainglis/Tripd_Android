package com.dainglis.trip_planner.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dainglis.trip_planner.services.EventDateService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, EventDateService.class); //When the broadcast receives (in this case the BootCompleted)
                                                                            //We start the service for notify the user
        context.startService(serviceIntent);
    }
}
