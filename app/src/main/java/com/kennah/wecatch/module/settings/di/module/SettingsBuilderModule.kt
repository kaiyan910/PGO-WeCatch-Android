package com.kennah.wecatch.module.settings.di.module

import android.support.v4.app.Fragment
import com.kennah.wecatch.module.settings.di.component.SettingsFragmentSubComponent
import com.kennah.wecatch.module.settings.ui.fragment.SettingsFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        SettingsFragmentSubComponent::class
))
abstract class SettingsBuilderModule {

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragmentInjectorFactory(builder: SettingsFragmentSubComponent.Builder): AndroidInjector.Factory<out Fragment>
}