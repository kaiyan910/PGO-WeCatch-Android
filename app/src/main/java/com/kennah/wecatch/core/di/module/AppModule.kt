package com.kennah.wecatch.core.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kennah.wecatch.App
import com.kennah.wecatch.core.di.AppScope
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.di.FilterScope
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    @AppScope
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @AppScope
    fun provideLocationHelper(app: Application): LocationHelper = LocationHelper(app)

    @Provides
    @AppScope
    fun providePreference(app: Application): SharedPreferences = app.getSharedPreferences(app.packageName, 0)

    @Provides
    @AppScope
    fun provideFilterManager(preference: SharedPreferences): FilterManager = FilterManager(preference)
}