package com.dainglis.trip_planner;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class TripdWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Remaining work: 1. Connect the widget to the content provider
        // 2. Comment well
        // 3. Write document

        CharSequence widgetText = "This is an extremely l o o o o o ng name for a trip";//context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.tripd_widget);
        views.setTextViewText(R.id.widget_trip_name, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

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
}

