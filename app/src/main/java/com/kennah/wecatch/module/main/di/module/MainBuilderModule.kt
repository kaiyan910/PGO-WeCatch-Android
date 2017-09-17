package com.kennah.wecatch.module.main.di.module

import android.support.v4.app.Fragment
import com.kennah.wecatch.module.main.di.component.MainFragmentSubComponent
import com.kennah.wecatch.module.main.ui.fragment.MainFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        MainFragmentSubComponent::class
))
abstract class MainBuilderModule {

    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    abstract fun bindMainFragmentInjectorFactory(builder: MainFragmentSubComponent.Builder): AndroidInjector.Factory<out Fragment>
}