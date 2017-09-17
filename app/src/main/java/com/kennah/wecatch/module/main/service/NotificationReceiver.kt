package com.kennah.wecatch.module.main.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.Prefs

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

        when (intent.action) {
            Constant.NOTIFICATION_ACTION_STOP -> {
                context.stopService(Intent(context, NotificationService::class.java))
                prefs.setNotifySwitch(false)

                LogUtils.debug("DEBUG#NotificationReceiver", "prefs.setNotifySwitch(false)")
            }
            Constant.NOTIFICATION_ACTION_STOP_MAP -> {
                context.stopService(Intent(context, MainService::class.java))
            }
            else -> {
            }
        }
    }
}