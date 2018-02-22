package pl.maxmati.tobiasz.mmos.bread.widget;

import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;
import pl.maxmati.tobiasz.mmos.bread.api.user.UserManager;
import pl.maxmati.tobiasz.mmos.bread.push.PushUpdateService;

public class BreadWidget extends AppWidgetProvider {
    public static final String RESOURCE_NAME = "bread";

    private static final String TAG = "BreadWidget";

    private static final String TOPIC_NAME = "counter_" + RESOURCE_NAME;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        PushUpdateService.subscribe(TOPIC_NAME);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Update requested");
        updateCounter(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "Widget disabled");

        cancelNotifications(context);
        PushUpdateService.unsubscribe(TOPIC_NAME);

        SessionManager.clearStore(context);
        UserManager.clearStore(context);
    }

    static void updateCounter(Context context) {
        Intent updaterIntent = new Intent(context, BreadWidgetUpdater.class);
        context.startService(updaterIntent);
    }

    private void cancelNotifications(Context context) {
        Log.d(TAG, "Canceling notifications");
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }
}

