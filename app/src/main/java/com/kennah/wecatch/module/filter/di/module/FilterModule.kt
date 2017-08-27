package com.kennah.wecatch.module.filter.di.module

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.kennah.wecatch.module.filter.di.FilterScope
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.filter.ui.adapter.FilterPagerAdapter
import dagger.Module
import dagger.Provides

@Module
class FilterModule {

    @Provides
    @FilterScope
    fun provideFragmentManager(activity: FilterActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    @FilterScope
    fun provideFilterPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter = FilterPagerAdapter(fm)
}