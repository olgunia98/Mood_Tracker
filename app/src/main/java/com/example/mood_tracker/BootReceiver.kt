package com.example.mood_tracker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mood_tracker.MainActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

        }
    }
}
