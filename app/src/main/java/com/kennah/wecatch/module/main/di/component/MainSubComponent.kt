package com.kennah.wecatch.module.main.di.component

import com.kennah.wecatch.module.main.di.MainScope
import com.kennah.wecatch.module.main.di.module.MainBuilderModule
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@MainScope
@Subcomponent(modules = arrayOf(MainBuilderModule::class))
interface MainSubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>()
}