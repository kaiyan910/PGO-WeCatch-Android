package com.kennah.wecatch.core.di.component

import android.app.Application
import com.kennah.wecatch.App
import com.kennah.wecatch.core.di.AppScope
import com.kennah.wecatch.core.di.module.AppModule
import com.kennah.wecatch.core.di.module.BuilderModule
import com.kennah.wecatch.core.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named

@AppScope
@Component(modules = arrayOf(
        AppModule::class,
        NetworkModule::class,
        BuilderModule::class,
        AndroidSupportInjectionModule::class
))
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        @BindsInstance
        fun server(@Named("server") url: String): Builder
        fun build(): AppComponent
    }
}