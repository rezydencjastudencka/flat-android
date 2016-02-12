package pl.maxmati.tobiasz.mmos.bread.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

import pl.maxmati.tobiasz.mmos.bread.R;

public class BreadWidgetUpdater extends Service {
    private static final String TAG = "BreadWidgetUpdater";

    public BreadWidgetUpdater() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int breadCount = getBreadCount();
        Log.d(TAG, "Got update request");

        updateWidget(breadCount);
        updateNotification(breadCount);
        return START_NOT_STICKY;
    }

    private void updateNotification(int breadCount) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder mBuilder;

        if(breadCount > 0) {
            mNotificationManager.cancelAll();
            Log.d(TAG, "Removing notification");
            return;
        }

        mBuilder = new Notification.Builder(this).setSmallIcon(R.drawable.bread)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_body));

        Log.d(TAG, "Creating notification");
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void updateWidget(int breadCount) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, BreadWidget.class), buildViews(breadCount));
    }

    private RemoteViews buildViews(int breadCount) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.bread_widget);
        views.setTextViewText(R.id.breadCounter, String.valueOf(breadCount));
        views.setOnClickPendingIntent(R.id.imageButton, PendingIntent.getService(this, 0, new Intent(this, BreadWidgetUpdater.class), 0));
        return views;
    }

    private int getBreadCount() {
        return new Random().nextInt() % 4;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
