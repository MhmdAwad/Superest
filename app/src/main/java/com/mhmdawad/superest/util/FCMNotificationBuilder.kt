package com.mhmdawad.superest.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mhmdawad.superest.R

class FCMNotificationBuilder(
    private val context: Context
) {
    private val buildNotificationID = "FCM_ID"
    private val notificationSequenceName = "FCM_NOTIFICATION"
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private fun notificationChannelBuilder() {
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           NotificationChannel(
                buildNotificationID,
                notificationSequenceName,
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.enableVibration(true)
                it.enableLights(true)
                notificationManager.createNotificationChannel(it)
            }
        }
    }

    fun buildNotification(title: String, message: String) {
        notificationChannelBuilder()
        notificationBuilder = NotificationCompat.Builder(context, buildNotificationID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
        notification = notificationBuilder.build()
        notificationManager.notify(1, notification)
    }
}