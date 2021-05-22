package cat.simple.alarms

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class HelperNotification {
    companion object {
        fun createNotificationChannel(
            context: Context,
            importance: Int,
            showBadge: Boolean,
            name: String,
            description: String
        ) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}