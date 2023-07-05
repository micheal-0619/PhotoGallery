package com.android.photogallery.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.photogallery.worker.PollWorker

abstract class VisibleFragment : Fragment() {
    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(requireContext(), "Got a broadcast: ${intent.action}", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onStart() {
        super.onStart()
        //注册广播
        val filter = IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION)
        requireActivity().registerReceiver(
            onShowNotification,
            filter,
            PollWorker.PERM_PRIVATE,
            null
        )
    }

    override fun onStop() {
        super.onStop()
        //注销广播
        requireActivity().unregisterReceiver(onShowNotification)
    }
}