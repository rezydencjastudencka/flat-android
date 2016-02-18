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

import pl.maxmati.tobiasz.mmos.bread.R;
import pl.maxmati.tobiasz.mmos.bread.api.APIAuthActivity;
import pl.maxmati.tobiasz.mmos.bread.api.APIConnector;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceManager;
import pl.maxmati.tobiasz.mmos.bread.api.resource.ResourceUpdateActivity;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionExpiredException;
import pl.maxmati.tobiasz.mmos.bread.api.session.SessionManager;

public class BreadWidgetUpdater extends Service {
    private static final String TAG = "BreadWidgetUpdater";

    public static final String EXTRA_RESOURCE_COUNT = "resourceCount";

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
        sendUpdateMessage(intent, startId);
        return START_STICKY;
    }

    private void sendUpdateMessage(Intent intent, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        if(intent != null && intent.hasExtra(EXTRA_RESOURCE_COUNT))
            msg.obj = intent.getIntExtra(EXTRA_RESOURCE_COUNT, 0);
        mServiceHandler.sendMessage(msg);
    }

    private void updateMissingNotification(int breadCount) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context
                .NOTIFICATION_SERVICE);

        if(breadCount > 0) {
            mNotificationManager.cancelAll();
            Log.d(TAG, "Removing notification");
            return;
        }
        Log.d(TAG, "Creating notification");
        mNotificationManager.notify(0, getUpdateNotification());
    }

    private Notification getUpdateNotification() {
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setSmallIcon(R.drawable.bread);
        mBuilder.setContentTitle(getString(R.string.notification_title_missing))
                .setContentText(getString(R.string.notification_content_missing));
        mBuilder.setPriority(Notification.PRIORITY_MAX).setOngoing(true);
        mBuilder.setContentIntent(getUpdatePendingIntent());
        return mBuilder.build();
    }

    private void updateWidget(int breadCount) {
        setWidgetsViews(buildViews(breadCount));
    }

    private RemoteViews buildViews(int breadCount) {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.bread_widget);
        views.setTextViewText(R.id.breadCounter, String.valueOf(breadCount));
        views.setOnClickPendingIntent(R.id.imageButton, getUpdatePendingIntent());
        return views;
    }

    private PendingIntent getUpdatePendingIntent() {
        Intent updateActivityIntent = new Intent(this, BreadWidgetUpdateActivity.class);
        updateActivityIntent.putExtra(ResourceUpdateActivity.RESOURCE_FIELD_NAME, "bread");
        return PendingIntent.getActivity(this, 0, updateActivityIntent, PendingIntent
                .FLAG_UPDATE_CURRENT);
    }

    private int getBreadCount() throws SessionException {
        APIConnector apiConnector = new APIConnector(SessionManager.restoreSession(
                BreadWidgetUpdater.this));
        try {
            return ResourceManager.get(apiConnector, BreadWidget.RESOURCE_NAME);
        } catch (HttpClientErrorException e) {
            throw new SessionException("Cannot get bread count", e);
        }
    }

    private void showAuthNotification() {
        Notification.Builder notificationBuilder = new Notification.Builder(this);

        notificationBuilder.setSmallIcon(R.drawable.bread);
        notificationBuilder.setContentTitle(getString(R.string.notification_title_authentication))
                .setContentText(getString(R.string.notification_content_authentication));
        // TODO: use for >= LOLLIPOP
        // notificationBuilder.setCategory(Notification.CATEGORY_ERROR);

        notificationBuilder.setContentIntent(getReauthPendingIntent());

        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(0,
                notificationBuilder.build());
    }

    private RemoteViews buildAuthViews() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.bread_widget);
        views.setTextViewText(R.id.breadCounter, getString(R.string.widget_missing_placeholder));
        views.setOnClickPendingIntent(R.id.imageButton, getReauthPendingIntent());
        return views;
    }

    private void setWidgetsViews(RemoteViews remoteViews) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(new ComponentName(this, BreadWidget.class), remoteViews);
    }

    private PendingIntent getReauthPendingIntent() {
        Intent apiAuthIntent = new Intent(this, APIAuthActivity.class);
        apiAuthIntent.putExtra(APIAuthActivity.EXTRA_AUTH_NOTIFICATION_ID, 0);
        apiAuthIntent.putExtra(APIAuthActivity.EXTRA_AUTH_SERVICE_INTENT, new Intent(this,
                BreadWidgetUpdater.class));
        return PendingIntent.getActivity(this, 0, apiAuthIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
            Log.d(TAG, "Got update request (widget id: " + msg.arg1 + ")");

            try {
                int breadCount = msg.obj != null ? (int) msg.obj : getBreadCount();
                updateWidget(breadCount);
                updateMissingNotification(breadCount);
            } catch (SessionExpiredException e) {
                Log.w(TAG, "Session expired, bringing authentication notification");
                setWidgetsViews(buildAuthViews());
                showAuthNotification();
            } catch (SessionException e) {
                Log.e(TAG, "Update failed: " + e.getMessage());
            }

            stopSelf(msg.arg1);
        }
    }
}
