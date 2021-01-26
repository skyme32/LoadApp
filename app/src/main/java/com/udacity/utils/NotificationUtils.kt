package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.MainActivity
import com.udacity.R
import com.udacity.detail.DetailActivity


const val NOTIFICATION_ID = 0

/**
 * Builds and delivers a notification.
 *
 * @param messageBody, notification text.
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    textFilename: String,
    status: Boolean
) {
    var intentContent = Intent(applicationContext, DetailActivity::class.java).apply {
        putExtra(MainActivity.EXTRA_MESSAGE, textFilename)
        putExtra(MainActivity.EXTRA_MESSAGE_STATE, status)
    }

    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intentContent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())

}