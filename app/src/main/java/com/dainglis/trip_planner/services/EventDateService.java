package com.dainglis.trip_planner.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.dainglis.trip_planner.R;
import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.models.Event;
import com.dainglis.trip_planner.providers.TripDataContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventDateService extends Service {

    public EventDateService() {
    }
    private boolean isRunning; //Bool to determine if the task is already running or not
    private Context context;
    private Thread backgroundThread; //Variable that will receive the background task
    @Override
    public IBinder onBind(Intent intent){
        return null;
    } //IBinder, which is mandatory

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

    Method:         onCreate()
    Description:    When the service is createad, we set the default value of our variables
    Parameters:
    Returns:        void.

--------------------------------------------------------------------------------------------- */
    @Override
    public void onCreate(){
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(checkDateTask);

    }
    @Override
    public void onDestroy(){        this.isRunning = false;    } //When destroy we only stop running the background task

    /* METHOD HEADER COMMENT -----------------------------------------------------------------------

    Method:         onStartCommand()
    Description:    When the service receives a "start", the onStartCommand initializes the background task.
                    The background task will iterates through all events and verify if the event's time is close.
                    If it is, we notify the user.
    Parameters:     Context         context
    Returns:        void.

--------------------------------------------------------------------------------------------- */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    Runnable checkDateTask = new Runnable() {
        @Override
        public void run() {//Start our background task

            while(true) {
                List<Event> eventList = TripDatabase.getInstance().eventDAO().getAllStatic(); //Get the list of all events
                for (Event e : eventList) { //Do a foreach
                    if (compareDates(e.date)) { //If the date of the event is less than 1 hour away
                        setNotification(e); //We call the notification
                        eventList.remove(e);//And remove the event from the list
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
        /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:        compareDates(String date)
        Description:    Receivesd a date and compares it to the current date in order to check if its difference
                        is smaller than 1 hour. If it is, we return true
        Parameters:     String date - Event date
        Returns:        boolean

    --------------------------------------------------------------------------------------------- */
    public boolean compareDates(String date){
        Date eventDate = null;
        Date currentTime = null;
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        try {
            eventDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date);
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(currentDateandTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = eventDate.getTime() - currentTime.getTime();
        long days = (int) (difference / (1000*60*60*24));
        long hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        hours = (hours < 0 ? -hours : hours);

        if(days == 0 && hours == 0){
            return true;
        } else{
            return false;
        }
    }
            /* METHOD HEADER COMMENT -----------------------------------------------------------------------

        Method:        setNotification(Event e)
        Description:    Receives data of the event that is close and notify the user.
        Parameters:     Event e - Event object
        Returns:        void

    --------------------------------------------------------------------------------------------- */

    protected void setNotification(Event e) {
        Intent notificationIntent = new Intent(this, ListActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int piFlag = PendingIntent.FLAG_UPDATE_CURRENT;
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent,piFlag);

        CharSequence tickerText = "MyTickerText";
        CharSequence contentTitle = "Friendly Reminder!";
        CharSequence contentText = "Don't forget that we have" + e.title + " starting in less than 1 hour!";

        NotificationCompat.Builder myBuilder = new NotificationCompat.Builder(this, "My Channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setTicker(tickerText)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0 stuff. Probably a good idea to use with the latest SDK

            CharSequence name = "Don't miss it out!";
            String description = "Don't forget that we have" + e.title + " starting in less than 1 hour!";
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel("My Channel", name, NotificationManagerCompat.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            manager.createNotificationChannel(channel);
        }

        manager.notify(1,myBuilder.build());

    }
}
