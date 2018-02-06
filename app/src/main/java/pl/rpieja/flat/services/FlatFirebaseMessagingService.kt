package pl.rpieja.flat.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.rpieja.flat.R
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.dto.Charge
import pl.rpieja.flat.dto.User


class FlatFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private val TAG = FlatFirebaseMessagingService::class.simpleName

        private const val TYPE = "type"
        private const val NEW_CHARGE = "new_charge"
        private const val CHARGE_ID = "charge_id"

        private const val CHARGES_CHANNEL = "charges"
        private const val CHARGES_GROUP = "charges"

        private const val SUMMARY_NOTIFICATION_ID = Int.MAX_VALUE
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        message!!

        Log.d(TAG, "From: " + message.from)

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + message.data)

            when (message.data[TYPE]) {
                NEW_CHARGE -> createNewChargeNotification(message.data[CHARGE_ID]!!.toInt())
            }

        }

    }

    private fun createNewChargeNotification(charge_id: Int) {
        Log.d(TAG, "Creating new charge " + charge_id)

        val flatAPI = FlatAPI.getFlatApi(this)
        val charge = flatAPI.fetchCharge(charge_id)
        val user = flatAPI.fetchUser(charge.from)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createNotificationChannel())
        }

        notificationManager.notify(SUMMARY_NOTIFICATION_ID, buildSummary())
        notificationManager.notify(charge_id, buildNotification(charge, user))
    }

    private fun buildNotification(charge: Charge, user: User): Notification {
        return NotificationCompat.Builder(this, CHARGES_CHANNEL)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(charge.name)
                .setGroup(CHARGES_GROUP)
                .setContentText(getString(R.string.notification_charges_text,
                        user.name, charge.amount, charge.name))
                .build()
    }

    private fun buildSummary(): Notification {
        return NotificationCompat.Builder(this, CHARGES_CHANNEL)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.notification_charges_summary_title))
                .setContentText(getString(R.string.notification_charges_summary_text))
                .setGroup(CHARGES_GROUP)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = getString(R.string.notification_charges_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(CHARGES_CHANNEL, name, importance)
    }


}