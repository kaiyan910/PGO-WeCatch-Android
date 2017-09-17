package com.kennah.wecatch.module.settings.di.component

import com.kennah.wecatch.module.settings.di.SettingsFragmentScope
import com.kennah.wecatch.module.settings.di.module.SettingsFragmentModule
import com.kennah.wecatch.module.settings.ui.fragment.SettingsFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@SettingsFragmentScope
@Subcomponent(modules = arrayOf(SettingsFragmentModule::class))
interface SettingsFragmentSubComponent : AndroidInjector<SettingsFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SettingsFragment>()
}