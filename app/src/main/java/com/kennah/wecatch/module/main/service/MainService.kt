package com.kennah.wecatch.module.main.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.kennah.wecatch.R
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import com.kennah.wecatch.module.main.ui.view.FloatingMapView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class MainService : Service() {

    private val FOREGROUND_ID = 998

    @Inject
    lateinit var mFloatingView: FloatingMapView

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(FOREGROUND_ID, createNotification())
        return START_STICKY
    }

    override fun onCreate() {

        AndroidInjection.inject(this)
        super.onCreate()

        mFloatingView.addCloseCallback {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mFloatingView.close()
        super.onDestroy()
        stopForeground(true)
    }


    private fun createNotification(): Notification {

        val intent = Intent(this, MainActivity::class.java)

        return NotificationCompat.Builder(this, "wecatch-pokemon")
                .setContentTitle(getText(R.string.app_name))
                .setContentText(getText(R.string.notification_foreground_map_message))
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimaryDark))
                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
                .addAction(createStopAction())
                .build()
    }

    private fun createStopAction(): NotificationCompat.Action {

        val intent = Intent()
        intent.action = Constant.NOTIFICATION_ACTION_STOP_MAP

        val intentPending = PendingIntent.getBroadcast(this, Constant.NOTIFICATION_BROADCAST, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Action.Builder(R.drawable.ic_stop, resources.getString(R.string.notification_foreground_stop), intentPending).build()
    }
}