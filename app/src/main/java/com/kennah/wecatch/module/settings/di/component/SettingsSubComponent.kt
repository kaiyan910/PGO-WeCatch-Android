package com.kennah.wecatch.module.settings.di.component

import com.kennah.wecatch.module.settings.di.SettingsScope
import com.kennah.wecatch.module.settings.di.module.SettingsBuilderModule
import com.kennah.wecatch.module.settings.ui.activity.SettingsActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@SettingsScope
@Subcomponent(modules = arrayOf(SettingsBuilderModule::class))
interface SettingsSubComponent : AndroidInjector<SettingsActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SettingsActivity>()
}