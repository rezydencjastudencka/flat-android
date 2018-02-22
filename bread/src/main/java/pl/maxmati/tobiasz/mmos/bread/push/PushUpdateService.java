package pl.maxmati.tobiasz.mmos.bread.push;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pl.maxmati.tobiasz.mmos.bread.widget.BreadWidgetUpdater;

/**
 * Created by mmos on 13.08.16.
 *
 * @author mmos
 */
public class PushUpdateService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        startService(new Intent(this, BreadWidgetUpdater.class));
    }

    public static void subscribe(String topic) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
    }

    public static void unsubscribe(String topic) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
