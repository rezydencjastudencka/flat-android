package pl.maxmati.tobiasz.flat.bread.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import pl.maxmati.tobiasz.flat.bread.widget.BreadWidgetUpdater;

/**
 * Created by mmos on 17.02.16.
 *
 * @author mmos
 */
public class ListenerService extends GcmListenerService {
    private static final String TAG = "GCMListener";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "GCM message received");
        // TODO: implement multiple topics handling
        startService(new Intent(this, BreadWidgetUpdater.class));
    }
}
