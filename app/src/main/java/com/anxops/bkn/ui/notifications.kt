package com.anxops.bkn.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.anxops.bkn.MainActivity
import com.anxops.bkn.R
import com.anxops.bkn.ui.navigation.DeepLinkDestination
import com.ramcosta.composedestinations.spec.Direction
import java.util.UUID


sealed class NotificationData(
    open val title: String,
    open val text: String
) {
    class Simple(
        override val title: String,
        override val text: String,
    ) : NotificationData(title, text)

    class DeepLink(
        override val title: String,
        override val text: String,
        val to: DeepLinkDestination,
        val params: Map<String, Any>? = null
    ) : NotificationData(title, text)

    class DestinationDeepLink(
        override val title: String,
        override val text: String,
        val to: Direction,
    ) : NotificationData(title, text)
}

class Notifier {

    companion object {
        const val CHANNEL_ID = "BIKENANCE_CHANNEL"
    }


    fun show(context: Context, data: NotificationData): Int {
        val notificationId: Int = genNotificationId()
        val intent = getIntent(context, data)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_link)
            .setContentTitle(data.title)
            .setContentText(data.text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
        return notificationId
    }

    private fun getIntent(context: Context, data: NotificationData): Intent {
        return when (data) {
            is NotificationData.Simple -> {
                Intent(context, MainActivity::class.java)
            }
            is NotificationData.DeepLink -> {
                Intent(
                    Intent.ACTION_VIEW,
                    buildDeepLinkUri(data),
                    context,
                    MainActivity::class.java
                )
            }
            is NotificationData.DestinationDeepLink -> {
                Intent(
                    Intent.ACTION_VIEW,
                    buildDestinationDeepLinkUri(data),
                    context,
                    MainActivity::class.java
                )
            }
        }.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private fun buildDeepLinkUri(data: NotificationData.DeepLink): Uri? {
        val params = data.params?.map { entry ->
            "${entry.key}=${entry.value}"
        }?.joinToString("&")
        return "bikenance://${data.to.value}${params?.let { "?$it" }}".toUri()
    }

    private fun buildDestinationDeepLinkUri(data: NotificationData.DestinationDeepLink): Uri? {
        return "bikenance://notification?route=${data.to.route}".toUri()
    }


    private fun genNotificationId(): Int {
        return UUID.randomUUID().hashCode()
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "BIKENANCE_CHANNEL"
            val descriptionText = "BIKENANCE NOTIFICATIONS"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}


