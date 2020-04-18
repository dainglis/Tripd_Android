/* SOURCE FILE HEADER COMMENT ======================================================================

    FILENAME:       TripdWidget.java
    PROJECT:        PROG3150 - Assignment 03
    PROGRAMMERS:    David Inglis, Nick Iden, Steven Knapp, Michel Gomes Lima, Megan Bradshaw
    DATE:           April 16th, 2020
    DESCRIPTION:    This file contains the interaction logic for the homescreen TripdWidget

================================================================================================= */

package com.dainglis.trip_planner;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.dainglis.trip_planner.controllers.TripDatabase;
import com.dainglis.trip_planner.providers.TripDataContentProvider;
import com.dainglis.trip_planner.providers.TripDataContract;
import com.dainglis.trip_planner.views.MainActivity;
import com.dainglis.trip_planner.views.TripInfoFragment;
import com.dainglis.trip_planner.views.TripListFragment;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class TripdWidget extends AppWidgetProvider {
    static void updateAppWidget(final Context context,
                                final AppWidgetManager appWidgetManager,
                                int appWidgetId,
                                final Cursor resultCursor) {

        // Create a pending intent for the activity
        // TripInfoFragment class is not TaskListActivity
        //Intent intent = new Intent(context, );


        //"Get the layout and set the listener for the app widget

        //views.setOnClickPendingIntent(R.id.widget_id, pendingIntent);

        RemoteViews views;
        Intent widgetAppIntent = new Intent(context, MainActivity.class).setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetAppIntent, 0);


        if (resultCursor != null && resultCursor.moveToFirst()) {

            //"Get the layout and set the listener for the app widget
            views = new RemoteViews(context.getPackageName(), R.layout.tripd_widget);

            // "Update the user interface to reflect the trip info
            views.setTextViewText(R.id.widget_trip_name,
                    resultCursor.getString(resultCursor.getColumnIndex("tripTitle")));
            views.setTextViewText(R.id.widget_startCity,
                    resultCursor.getString(resultCursor.getColumnIndex("startLocation")));
            views.setTextViewText(R.id.widget_endCity,
                    resultCursor.getString(resultCursor.getColumnIndex("endLocation")));
            views.setTextViewText(R.id.widget_departDate,
                    resultCursor.getString(resultCursor.getColumnIndex("startDate")));
            views.setTextViewText(R.id.widget_arriveDate,
                    resultCursor.getString(resultCursor.getColumnIndex("endDate")));

            widgetAppIntent.putExtra(TripDataContract.KEY_TRIP_ID,
                    resultCursor.getLong(resultCursor.getColumnIndex("tripId"))); // THIS SHOULD try to make the main activity swap to the correct trip. I'm not sure it's getting the correct ID though.

            // Set the on-click action
            views.setOnClickPendingIntent(R.id.widget_id, pendingIntent);

        }
        else {

            // Otherwise set the widget view to "tripd_widget_none"
            Log.e("Widget", "Received null Cursor object");
            views =  new RemoteViews(context.getPackageName(), R.layout.tripd_widget_none);

            views.setOnClickPendingIntent(appWidgetId, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    public Cursor getAvailableTrip(final Context context) {

        // Get the text from the database / content provider to display within the widget
        SimpleDateFormat dateFormat = new SimpleDateFormat(TripDataContract.DATE_FORMAT, Locale.CANADA);

        Date today = Calendar.getInstance().getTime();
        Log.d("Widget", "Current date: " + dateFormat.format(today));

        Uri uri = (new Uri.Builder()).scheme("content")
                .authority(TripDataContract.AUTHORITY)
                .appendPath("trips")
                .appendPath("by")
                .build();

        return context.getContentResolver()
                .query(uri,
                        null,
                        dateFormat.format(today),
                        null,
                        null);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Cursor tripCursor = getAvailableTrip(context);

                if (tripCursor != null) {
                    // There may be multiple widgets active, so update all of them
                    for (int appWidgetId : appWidgetIds) {
                        updateAppWidget(context, appWidgetManager, appWidgetId, tripCursor);
                    }
                    tripCursor.close();
                }
            }
        });

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

                                    // TripDataContentProvider is not TaskListDB.TASK_MODIFIED =====================================
        if (intent.getAction().equals(TripDataContentProvider.class.getName())) {

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName provider = new ComponentName(context, TripdWidget.class);

            int [] appWidgetIds = manager.getAppWidgetIds(provider);
            onUpdate(context, manager, appWidgetIds);

        }

    }

}

