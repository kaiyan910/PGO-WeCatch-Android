package com.kennah.wecatch

import android.app.Activity
import android.app.Application
import android.support.multidex.MultiDexApplication
import com.kennah.wecatch.core.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var mDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    init {
        instance = this
    }

    companion object {
        lateinit var instance:App
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
                .application(this)
                .server("https://www.wecatch.net")
                .build()
                .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mDispatchingAndroidInjector
}