package com.kennah.wecatch.module.filter.di.module

import android.app.Application
import android.support.v4.app.FragmentManager
import com.kennah.wecatch.R
import com.kennah.wecatch.module.filter.di.FilterScope
import com.kennah.wecatch.module.filter.ui.activity.FilterActivity
import com.kennah.wecatch.module.filter.ui.adapter.FilterPagerAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class FilterModule {

    @Provides
    @FilterScope
    fun provideFragmentManager(activity: FilterActivity): FragmentManager = activity.supportFragmentManager

    @Provides
    @FilterScope
    @Named("tabs")
    fun provideFilterTabs(app: Application): Array<String> = app.resources.getStringArray(R.array.filter_tab)

    @Provides
    @FilterScope
    fun provideFilterPagerAdapter(fm: FragmentManager, @Named("tabs") tabs: Array<String>): FilterPagerAdapter = FilterPagerAdapter(fm, tabs)
}