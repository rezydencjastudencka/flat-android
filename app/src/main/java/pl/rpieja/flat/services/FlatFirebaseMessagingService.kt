package pl.rpieja.flat.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.rpieja.flat.R
import pl.rpieja.flat.activity.MainActivity
import pl.rpieja.flat.api.FlatAPI
import pl.rpieja.flat.api.FlatApiException
import pl.rpieja.flat.api.UnauthorizedException
import pl.rpieja.flat.dto.Expense


class FlatFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private val TAG = FlatFirebaseMessagingService::class.simpleName

        private const val TYPE = "type"
        private const val NEW_EXPENSE = "new_expense"
        private const val EXPENSE_ID = "expense_id"

        private const val EXPENSES_CHANNEL = "expenses"
        private const val EXPENSES_GROUP = "expenses"

        private const val SUMMARY_NOTIFICATION_ID = Int.MAX_VALUE
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        message!!

        Log.d(TAG, "From: " + message.from)

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + message.data)

            when (message.data[TYPE]) {
                NEW_EXPENSE -> createNewExpenseNotification(message.data[EXPENSE_ID]!!.toInt())
            }

        }

    }

    private fun createNewExpenseNotification(expense_id: Int) {
        Log.d(TAG, "Creating new expense $expense_id")

        try {
            val flatAPI = FlatAPI.getFlatApi(this)
            val expense = flatAPI.fetchExpense(expense_id).blockingGet()

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(createNotificationChannel())
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                notificationManager.notify(SUMMARY_NOTIFICATION_ID, buildSummary())
            }

            notificationManager.notify(expense_id, buildNotification(expense))
        } catch (e: FlatApiException) {
            Log.d(TAG,"Unable to fetch expense: $expense_id", e)
        }
    }

    private fun buildNotification(expense: Expense): Notification {
        val resultIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_ONE_SHOT)

        return NotificationCompat.Builder(this, EXPENSES_CHANNEL)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(expense.name)
                .setGroup(EXPENSES_GROUP)
                .setContentText(getString(R.string.notification_expenses_text,
                        expense.from.name, expense.amount, expense.name))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun buildSummary(): Notification {
        return NotificationCompat.Builder(this, EXPENSES_CHANNEL)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(getString(R.string.notification_expenses_summary_title))
                .setContentText(getString(R.string.notification_expenses_summary_text))
                .setGroup(EXPENSES_GROUP)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .setAutoCancel(true)
                .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = getString(R.string.notification_expenses_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(EXPENSES_CHANNEL, name, importance)
    }

    override fun onNewToken(refreshedToken: String?) {
        try {
            FlatAPI.getFlatApi(this).registerFCM(refreshedToken!!)
        } catch (e: UnauthorizedException){
            //Do nothing we will register on login
        }
    }
}