package pl.maxmati.tobiasz.mmos.bread.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import pl.maxmati.tobiasz.mmos.bread.R;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "GCMRegistration";

    public static final String EXTRA_TOPIC = "topic";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String topic;

            Log.d(TAG, "Registering into GCM service");
            if(!intent.hasExtra(EXTRA_TOPIC) || (topic = intent.getStringExtra(EXTRA_TOPIC)).isEmpty()) {
                Log.d(TAG, "No topic provided, aborting registration");
                return;
            }

            // TODO: token storage
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e(TAG, "New token registered");

            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
            Log.d(TAG, "Subscribed topic: " + topic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
