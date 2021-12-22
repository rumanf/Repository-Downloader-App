package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0


fun NotificationManager.sendNotification(messageBody: String,messagestring:String, downloadStatus:String, applicationContext: Context) {

    val Selected_Item=messagestring
    val Download_Status=downloadStatus

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("selected item",Selected_Item)
    contentIntent.putExtra("download status",Download_Status)

    val contentPendingIntent: PendingIntent? = TaskStackBuilder.create(applicationContext).run {
        addNextIntentWithParentStack(contentIntent)
        getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    // create style
    val displayImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_launcher_background
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(displayImage)
        .bigLargeIcon(null)


    val builder = NotificationCompat.Builder(
        applicationContext,
        "Notification1"
    )
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Download Status")
        .setContentText(messageBody)

        .setAutoCancel(true)
        .addAction(0,"See Details",contentPendingIntent)
        // style
        .setStyle(bigPicStyle)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}
    fun NotificationManager.cancelNotifications() {
        cancelAll()}

