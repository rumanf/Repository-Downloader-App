package com.udacity

import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
   // private lateinit var pendingIntent: PendingIntent
   // private lateinit var action: NotificationCompat.Action
    private lateinit var downloadManager: DownloadManager
    private lateinit var downloadstatus:String
    private lateinit var messagestring:String
    var selectedItem: String = " "
    var selectedUrl = URL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        // create channel
        createChannel("Notification1", "Notification1")

        // notification manager set
        notificationManager = ContextCompat.getSystemService(
            this@MainActivity,
            NotificationManager::class.java
        ) as NotificationManager



        //radio buttons click listener and setting value based on selected item
        radioGroup.setOnCheckedChangeListener { radioGroup, itemselected ->
            if (itemselected == R.id.retrofitButton) {
                selectedItem = "retrofit"
                messagestring=getString(R.string.retrofit_string)
            }
            if (itemselected == R.id.loadappButton) {
                selectedItem = "app"
                messagestring=getString(R.string.currentapp_string)
            }
            if (itemselected == R.id.glideButton) {
                selectedItem = "glide"
                messagestring=getString(R.string.glide_string)
            }
        }

        custom_button.setOnClickListener {
            buttonanimation()
            if ( selectedItem != " "){
            download()
        }}

        // when download is complete, receiver gets it then sends notificaiton
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val query = DownloadManager.Query()
            query.setFilterById(id!!)

            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))



                if (status==DownloadManager.STATUS_SUCCESSFUL) {
                    downloadstatus="Success"
                }
                if (status==DownloadManager.STATUS_FAILED) {
                    downloadstatus="fail"
                }
            }


                notificationManager.sendNotification("The status of the $messagestring download is $downloadstatus ",messagestring,downloadstatus, this@MainActivity)}
        }


    private fun download() {

        if (selectedItem == "glide") {
            selectedUrl = URLglide
        }
        if (selectedItem == "app") {
            selectedUrl = URLretrofit
        }

        val request =
            DownloadManager.Request(Uri.parse(selectedUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                //  .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
        private const val URLretrofit =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val URLglide =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"

    }


    fun buttonanimation() {

        // if empty then do toast...
        if (selectedItem == " ") {
            // play the toast
            Toast.makeText(this, "Please select one of the 3 items", Toast.LENGTH_SHORT).show()
        } else {
            custom_button.animateProgress()
        }
    }
    // notification stuff

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                // TODO: Step 2.4 change importance
                NotificationManager.IMPORTANCE_HIGH
            )// TODO: Step 2.6 disable badges for this channel
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Status"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }
}
