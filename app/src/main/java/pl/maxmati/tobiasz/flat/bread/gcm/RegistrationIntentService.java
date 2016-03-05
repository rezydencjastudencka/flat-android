package pl.maxmati.tobiasz.flat.bread.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import pl.maxmati.tobiasz.mmos.bread.R;

public class RegistrationIntentService extends IntentService {
    public static final String ACTION_SUBSCRIBE_TOPIC = "subscribeTopic";
    public static final String ACTION_CANCEL_SUBSCRIBE_TOPIC = "cancelSubscribeTopic";
    public static final String EXTRA_TOPIC = "topic";

    private static final String TAG = "GCMRegistration";

    private static final String STORE_NAME = "GCMStore";
    private static final String STORE_FIELD_NAME_TOKEN = "token";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String topic;
        String token;

        if(!intent.hasExtra(EXTRA_TOPIC) || (topic = intent.getStringExtra(EXTRA_TOPIC))
                .isEmpty()) {
            Log.d(TAG, "No topic provided, aborting registration");
            return;
        }

        try {
            token = getToken();
        } catch (IOException e) {
            Log.e(TAG, "Cannot get GCM token: " + e.getMessage());
            return;
        }

        switch (intent.getAction()) {
            case ACTION_SUBSCRIBE_TOPIC:
                try {
                    subscribeTopic(token, topic);
                } catch (IOException e) {
                    Log.e(TAG, "Topic subscription failed: " + e.getMessage());
                    return;
                }
                break;
            case ACTION_CANCEL_SUBSCRIBE_TOPIC:
                try {
                    cancelSubscribeTopic(token, topic);
                } catch (IOException e) {
                    Log.e(TAG, "Topic subscription cancel failed: " + e.getMessage());
                    return;
                }
                break;
            default:
                Log.w(TAG, "Unhandled action " + intent.getAction());
        }
    }

    private void subscribeTopic(String token, String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.subscribe(token, "/topics/" + topic, null);
        Log.d(TAG, "Subscribed topic: " + topic);
    }

    private void cancelSubscribeTopic(String token, String topic) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        pubSub.unsubscribe(token, "/topics/" + topic);
        Log.d(TAG, "Canceled topic subscription: " + topic);
    }

    private static SharedPreferences getStore(Context context) {
        return context.getApplicationContext().getSharedPreferences(STORE_NAME, MODE_PRIVATE);
    }

    private String getToken() throws IOException {
        SharedPreferences sharedPreferences = getStore(this);
        if(sharedPreferences.contains(STORE_FIELD_NAME_TOKEN)) {
            Log.d(TAG, "GCM token restored");
            return sharedPreferences.getString(STORE_FIELD_NAME_TOKEN, null);
        }

        InstanceID instanceID = InstanceID.getInstance(this);
        String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        SharedPreferences.Editor storeEditor = sharedPreferences.edit();

        storeEditor.putString(STORE_FIELD_NAME_TOKEN, token);
        storeEditor.apply();
        Log.i(TAG, "New GCM token stored");

        return token;
    }

    public static void clearStore(Context context) {
        getStore(context).edit().clear().apply();
        Log.d(TAG, "Token store cleared");
    }
}
