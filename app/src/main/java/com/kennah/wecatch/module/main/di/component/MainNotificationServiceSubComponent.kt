package com.kennah.wecatch.module.main.di.component

import com.kennah.wecatch.module.main.di.MainNotificationServiceScope
import com.kennah.wecatch.module.main.di.MainServiceScope
import com.kennah.wecatch.module.main.di.module.MainNotificationServiceModule
import com.kennah.wecatch.module.main.di.module.MainServiceModule
import com.kennah.wecatch.module.main.service.MainService
import com.kennah.wecatch.module.main.service.NotificationService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@MainNotificationServiceScope
@Subcomponent(modules = arrayOf(MainNotificationServiceModule::class))
interface MainNotificationServiceSubComponent : AndroidInjector<NotificationService> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<NotificationService>()
}