package com.paytabs.memoria

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class NotificationHelper(private val context: Context?) {


    fun sendNotification() {
        createNotificationChannel(context)
        context?.let {
            val builder = NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_back_hand_24).setContentTitle("textTitle")
                .setContentText("textContent").setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(it)) {
                notify(100, builder.build())
            }
        }
    }

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context?.getString(R.string.channel_name)
            val descriptionText = context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            if (context != null) {
                val notificationManager: NotificationManager? =
                    ContextCompat.getSystemService(context, NotificationManager::class.java)
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

}