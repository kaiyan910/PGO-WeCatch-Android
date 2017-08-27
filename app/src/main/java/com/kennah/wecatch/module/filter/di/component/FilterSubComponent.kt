package com.kennah.wecatch.module.filter.di.component

import com.kennah.wecatch.module.filter.di.FilterScope
import com.kennah.wecatch.module.filter.di.module.FilterBuilderModule
import com.kennah.wecatch.module.filter.di.module.FilterModule
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.main.ui.activity.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@FilterScope
@Subcomponent(modules = arrayOf(FilterModule::class, FilterBuilderModule::class))
interface FilterSubComponent: AndroidInjector<FilterActivity> {

    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<FilterActivity>()
}