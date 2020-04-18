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


        // Remaining work:
        // 1. Connect the widget to the content provider
        // 2. Comment well
        // 3. Write document

        // Create a pending intent for the activity
        // TripInfoFragment class is not TaskListActivity
        //Intent intent = new Intent(context, );


        //"Get the layout and set the listener for the app widget

        //views.setOnClickPendingIntent(R.id.widget_id, pendingIntent);

        RemoteViews views;
        Intent widgetAppIntent = new Intent(context, MainActivity.class).setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, widgetAppIntent, 0);

        if (resultCursor != null && resultCursor.moveToFirst()) {
            views = new RemoteViews(context.getPackageName(), R.layout.tripd_widget);

            // "Update the user interface
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
                    resultCursor.getLong(resultCursor.getColumnIndex("tripId")));

            views.setOnClickPendingIntent(appWidgetId, pendingIntent);


        }
        else {
            Log.e("Widget", "Received null Cursor object");
            views =  new RemoteViews(context.getPackageName(), R.layout.tripd_widget_none);

            views.setOnClickPendingIntent(appWidgetId, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public Cursor getAvailableTrip(final Context context) {
        // Get the text from the database / content provider to display within the widget
        //TripDataContentProvider contentProvider = new TripDataContentProvider();
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



        /*
        The most important AppWidgetProvider callback is onUpdate() because it is called when each
        App Widget is added to a host (unless you use a configuration Activity). If your App Widget
        accepts any user interaction events, then you need to register the event handlers in this
        callback. If your App Widget doesn't create temporary files or databases, or perform other
        work that requires clean-up, then onUpdate() may be the only callback method you need to
        define. For example, if you want an App Widget with a button that launches an Activity when
        clicked, you could use the following implementation of AppWidgetProvider:

          public class ExampleAppWidgetProvider extends AppWidgetProvider {

                public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
                    final int N = appWidgetIds.length;

                    // Perform this loop procedure for each App Widget that belongs to this provider
                    for (int i=0; i<N; i++) {
                        int appWidgetId = appWidgetIds[i];

                        // Create an Intent to launch ExampleActivity
                        Intent intent = new Intent(context, ExampleActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                        // Get the layout for the App Widget and attach an on-click listener
                        // to the button
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider_layout);
                        views.setOnClickPendingIntent(R.id.button, pendingIntent);

                        // Tell the AppWidgetManager to perform an update on the current app widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                    }
                }
            }

            This AppWidgetProvider defines only the onUpdate() method for the purpose of defining a
            PendingIntent that launches an Activity and attaching it to the App Widget's button with
            setOnClickPendingIntent(int, PendingIntent). Notice that it includes a loop that
            iterates through each entry in appWidgetIds, which is an array of IDs that identify each
            App Widget created by this provider. In this way, if the user creates more than one
            instance of the App Widget, then they are all updated simultaneously. However, only one
            updatePeriodMillis schedule will be managed for all instances of the App Widget. For
            example, if the update schedule is defined to be every two hours, and a second instance
            of the App Widget is added one hour after the first one, then they will both be updated
            on the period defined by the first one and the second update period will be ignored
            (they'll both be updated every two hours, not every hour).

        * */
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
                                    // TripDataContentProvider is not TaskListDB.TASK_MODIFIED
        if (intent.getAction().equals(TripDataContentProvider.class.getName())) {

            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName provider = new ComponentName(context, TripdWidget.class);

            int [] appWidgetIds = manager.getAppWidgetIds(provider);
            onUpdate(context, manager, appWidgetIds);

        }

    }

}

