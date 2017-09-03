package com.kennah.wecatch.module.main.di.component

import com.kennah.wecatch.module.main.di.MainServiceScope
import com.kennah.wecatch.module.main.di.module.MainServiceModule
import com.kennah.wecatch.module.main.service.MainService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@MainServiceScope
@Subcomponent(modules = arrayOf(MainServiceModule::class))
interface MainServiceSubComponent: AndroidInjector<MainService> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<MainService>()
}