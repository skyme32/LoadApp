package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.detail.DetailActivity
import com.udacity.ui.ButtonState
import com.udacity.utils.NOTIFICATION_ID
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var textFilename = ""
    private var statuBoolean = false

    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setBtnState(ButtonState.Clicked)

        custom_button.setOnClickListener {
            when {
                radio_btn_glide.isChecked -> {
                    textFilename = getString(R.string.txt_glide_radiogroup)
                    download(URL_GLIDE)
                }
                radio_btn_loadapp.isChecked -> {
                    textFilename = getString(R.string.txt_loadapp_radiogroup)
                    download(URL_PROJECT)
                }
                radio_btn_retrofit.isChecked -> {
                    download(URL_RETROFIT)
                    textFilename = getString(R.string.txt_retrofit_radiogroup)
                }
                else -> {
                    if (edt_urlweb.text.isEmpty()) {
                        Toast.makeText(this, "Please, check the list", Toast.LENGTH_SHORT).show()
                    } else {
                        textFilename = edt_urlweb.text.toString()
                        download(edt_urlweb.text.toString())
                        Toast.makeText(this, textFilename, Toast.LENGTH_SHORT).show()
                    }

                }
            }

        }

        //Create the channel to notification
        createChannel(
                getString(R.string.notification_channel_id),
                getString(R.string.notification_description),
                getString(R.string.notification_title)
        )
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.i("statusDownload", id.toString())


            val c = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
            if (c.moveToFirst()) {
                val status: Int = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
                Log.i("statusDownload", status.toString())

                when (status) {
                    DownloadManager.STATUS_FAILED -> {
                        Log.i("statusDownload", "Failed")
                        statuBoolean = false
                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        Log.i("statusDownload", "Finish")
                        statuBoolean = true
                    }
                }

                sendNotification()
                c.close()
            }
        }
    }

    private fun download(url: String) {
        custom_button.setBtnState(ButtonState.Loading)

        try {
            val request =
                    DownloadManager.Request(Uri.parse(url))
                            .setTitle(getString(R.string.app_name))
                            .setDescription(getString(R.string.app_description))
                            .setRequiresCharging(false)
                            .setAllowedOverMetered(true)
                            .setAllowedOverRoaming(true)

            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        } catch (e: Exception) {
            statuBoolean = false
            sendNotification()
            Log.i("statusDownload", "FAILED")
        }
    }


    private fun createChannel(channel: String, description: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channel,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = description

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    private fun sendNotification() {
        notificationManager.sendNotification(
                getString(R.string.notification_description),
                applicationContext,
                textFilename,
                statuBoolean
        )

        radio_group.clearCheck()
        edt_urlweb.text.clear()
        custom_button.setBtnState(ButtonState.Completed)

        Log.i("statusDownload", "Send notification")
    }

    companion object {
        private const val URL_RETROFIT = "https://github.com/square/retrofit/archive/master.zip"
        private const val URL_PROJECT = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE = "https://github.com/bumptech/glide/archive/master.zip"
        const val EXTRA_MESSAGE = "com.udacity.MESSAGE"
        const val EXTRA_MESSAGE_STATE = "com.udacity.MESSAGE_STATE"
    }

}
