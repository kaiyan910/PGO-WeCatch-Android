package com.kennah.wecatch.module.main.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kennah.wecatch.module.main.ui.view.FloatingMapView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class MainService : Service() {

    @Inject
    lateinit var mFloatingView: FloatingMapView

    override fun onBind(p0: Intent?): IBinder? {
        return null
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
    }
}