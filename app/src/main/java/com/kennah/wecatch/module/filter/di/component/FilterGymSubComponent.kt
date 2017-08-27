package com.kennah.wecatch.module.filter.di.component

import com.kennah.wecatch.module.filter.di.FilterGymScope
import com.kennah.wecatch.module.filter.di.module.FilterGymFragmentModule
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@FilterGymScope
@Subcomponent(modules = arrayOf(FilterGymFragmentModule::class))
interface FilterGymSubComponent: AndroidInjector<FilterGymFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FilterGymFragment>()
}