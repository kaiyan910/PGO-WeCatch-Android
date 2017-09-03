package com.kennah.wecatch.core.di.module

import android.app.Activity
import android.app.Service
import com.kennah.wecatch.module.filter.di.component.FilterSubComponent
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.di.component.MainServiceSubComponent
import com.kennah.wecatch.module.main.di.component.MainSubComponent
import com.kennah.wecatch.module.main.service.MainService
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        MainSubComponent::class,
        FilterSubComponent::class,
        MainServiceSubComponent::class
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
    @ServiceKey(MainService::class)
    abstract fun bindMainServiceInjectorFactory(builder: MainServiceSubComponent.Builder): AndroidInjector.Factory<out Service>
}