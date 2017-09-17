package com.kennah.wecatch.core.di.module

import android.app.Activity
import android.app.Service
import com.kennah.wecatch.module.filter.di.component.FilterSubComponent
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.di.component.MainNotificationServiceSubComponent
import com.kennah.wecatch.module.main.di.component.MainServiceSubComponent
import com.kennah.wecatch.module.main.di.component.MainSubComponent
import com.kennah.wecatch.module.main.service.MainService
import com.kennah.wecatch.module.main.service.NotificationService
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import com.kennah.wecatch.module.settings.di.component.SettingsSubComponent
import com.kennah.wecatch.module.settings.ui.activity.SettingsActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        MainSubComponent::class,
        FilterSubComponent::class,
        SettingsSubComponent::class,
        MainServiceSubComponent::class,
        MainNotificationServiceSubComponent::class
))
abstract class BuilderModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    abstract fun bindMainActivityInjectorFactory(builder: MainSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(FilterActivity::class)
    abstract fun bindFilterActivityInjectorFactory(builder: FilterSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ActivityKey(SettingsActivity::class)
    abstract fun bindSettingsActivityInjectorFactory(builder: SettingsSubComponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ServiceKey(MainService::class)
    abstract fun bindMainServiceInjectorFactory(builder: MainServiceSubComponent.Builder): AndroidInjector.Factory<out Service>

    @Binds
    @IntoMap
    @ServiceKey(NotificationService::class)
    abstract fun bindNotificationServiceInjectorFactory(builder: MainNotificationServiceSubComponent.Builder): AndroidInjector.Factory<out Service>
}