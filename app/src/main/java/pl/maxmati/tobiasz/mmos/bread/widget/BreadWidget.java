package pl.maxmati.tobiasz.mmos.bread.widget;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BreadWidget extends AppWidgetProvider {
    public static final String RESOURCE_NAME = "bread";

    private static final String TAG = "BreadWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent updaterIntent;

        Log.d(TAG, "Update requested");

        updaterIntent = new Intent(context, BreadWidgetUpdater.class);
        updaterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.startService(updaterIntent);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "Widget disabled; canceling notifications");
        cancelNotifications(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    private void cancelNotifications(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }
}

