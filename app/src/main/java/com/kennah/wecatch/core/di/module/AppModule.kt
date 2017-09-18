package com.kennah.wecatch.core.di.module

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kennah.wecatch.App
import com.kennah.wecatch.core.di.AppScope
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.filter.FilterHandler
import com.kennah.wecatch.local.filter.FilterHandlerFactory
import com.kennah.wecatch.module.filter.di.FilterScope
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @AppScope
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @AppScope
    fun provideNotificationManager(app: Application): NotificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @AppScope
    fun providePreference(app: Application): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    @AppScope
    fun providePrefs(sharedPreferences: SharedPreferences): Prefs = Prefs(sharedPreferences)

    @Provides
    @AppScope
    fun provideFilterManager(preference: Prefs): FilterManager = FilterManager(preference)

    @Provides
    @AppScope
    fun provideFilterHandlerFactory(manager: FilterManager) = FilterHandlerFactory(manager)

    @Provides
    @AppScope
    fun provideOttoBus(): Bus = Bus()
}