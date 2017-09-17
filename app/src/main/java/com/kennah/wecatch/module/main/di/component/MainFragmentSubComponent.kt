package com.kennah.wecatch.module.main.di.component

import com.kennah.wecatch.module.main.di.MainFragmentScope
import com.kennah.wecatch.module.main.di.module.MainFragmentModule
import com.kennah.wecatch.module.main.ui.fragment.MainFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@MainFragmentScope
@Subcomponent(modules = arrayOf(MainFragmentModule::class))
interface MainFragmentSubComponent : AndroidInjector<MainFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainFragment>()
}