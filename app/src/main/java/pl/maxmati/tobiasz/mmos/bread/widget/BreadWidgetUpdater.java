package pl.maxmati.tobiasz.mmos.bread.widget;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceManager;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceUpdateActivity;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

public class BreadWidgetUpdater extends Service {
    private static final String TAG = "BreadWidgetUpdater";

    private ServiceHandler mServiceHandler;

    public BreadWidgetUpdater() {
    }

    @Override
    public void onCreate() {
        HandlerThread mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();

        Looper mServiceLooper = mHandlerThread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    private void updateNotification(int breadCount) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);
        Notification.Builder mBuilder;

        if(breadCount > 0) {
            mNotificationManager.cancelAll();
            Log.d(TAG, "Removing notification");
            return;
        }

        mBuilder = new Notification.Builder(this).setPriority(Notification.PRIORITY_MAX)
                .setOngoing(true).setSmallIcon(R.drawable.bread).setContentTitle
                        (getString(R.string.notification_title)).setContentText(getString(R.string.notification_body));

        Log.d(TAG, "Creating notification");
        mNotificationManager.notify(0, mBuilder.build());
    }

    private void updateWidget(int breadCount) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, BreadWidget.class), buildViews(breadCount));
    }

    private RemoteViews buildViews(int breadCount) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.bread_widget);
        Intent updateActivityIntent = new Intent(this, BreadWidgetUpdateActivity.class);

        views.setTextViewText(R.id.breadCounter, String.valueOf(breadCount));

        updateActivityIntent.putExtra(ResourceUpdateActivity.RESOURCE_FIELD_NAME, "bread");

        views.setOnClickPendingIntent(R.id.imageButton,
                PendingIntent.getActivity(this, 0, updateActivityIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));
        return views;
    }

    private int getBreadCount() throws SessionException {
        APIConnector apiConnector = new APIConnector(SessionManager.restoreSession(BreadWidgetUpdater.this));
        try {
            return ResourceManager.get(apiConnector, BreadWidget.RESOURCE_NAME);
        } catch (HttpClientErrorException e) {
            throw new SessionException("Cannot get bread count", e);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "Got update request");

            try {
                int breadCount = getBreadCount();
                updateWidget(breadCount);
                updateNotification(breadCount);
            } catch (SessionException e) {
                Log.d(TAG, "Update failed: " + e.getMessage());
            }

            stopSelf(msg.arg1);
        }
    }
}
