package com.kennah.wecatch

import android.content.Context
import android.support.multidex.MultiDex
import com.kennah.wecatch.core.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class App: DaggerApplication() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder()
            .application(this)
            .server("https://www.wecatch.net")
            .build()
}