package pl.maxmati.tobiasz.mmos.bread.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
        Log.d(TAG, "Got update request");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, BreadWidget.class), buildViews(getBreadCount()));
        return START_NOT_STICKY;
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
