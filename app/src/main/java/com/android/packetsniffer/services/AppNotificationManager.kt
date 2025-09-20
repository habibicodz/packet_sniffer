package com.android.packetsniffer.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.android.packetsniffer.BuildConfig
import com.android.packetsniffer.MainActivity

object AppNotificationManager {
    fun createProxyServiceNotification(context: Context): Notification {
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        val channelId: String = BuildConfig.APPLICATION_ID + "." + "ProxyServiceNotification"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId, "ProxyService Channel", NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val contentIntent = PendingIntent.getActivity(
            context, 0xFF, Intent(
                context,
                MainActivity::class.java
            ), PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, channelId)
            .setShowWhen(false)
            .setWhen(0)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setOngoing(true)
            .setContentIntent(
                contentIntent
            )
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }
}