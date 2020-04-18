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

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onCreate(){
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(checkDateTask);

    }
    @Override
    public void onDestroy(){
        this.isRunning = false;
    }
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
        public void run() {

            while(true) {
                List<Event> eventList = TripDatabase.getInstance().eventDAO().getAllStatic();
                for (Event e : eventList) {
                    if (compareDates(e.date)) {
                        setNotification(e);
                        eventList.remove(e);
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
            //If you need more, this URL has a good explanation of new things
            //https://developer.android.com/training/notify-user/build-notification.html

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
