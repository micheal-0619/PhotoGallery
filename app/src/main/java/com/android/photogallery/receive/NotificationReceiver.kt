package com.android.photogallery.receive

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.android.photogallery.worker.PollWorker

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "received broadcast: result: $resultCode")
        if (resultCode != Activity.RESULT_OK) {
            // A foreground activity canceled the broadcast
            return
        }
        val requestCode = intent.getIntExtra(PollWorker.REQUEST_CODE, 0)
        val notification: Notification? = intent.getParcelableExtra(PollWorker.NOTIFICATION)
        val notificationManager =
            NotificationManagerCompat.from(context)
        notification?.let { notificationManager.notify(requestCode, it) }
    }
}