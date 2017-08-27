package com.kennah.wecatch.module.filter.di.module

import android.app.Application
import android.support.v7.widget.RecyclerView
import com.kennah.wecatch.core.SimpleDividerItemDecoration
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.di.FilterGymScope
import com.kennah.wecatch.module.filter.ui.adapter.FilterGymListAdapter
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import dagger.Module
import dagger.Provides

@Module
class FilterGymFragmentModule {

    @Provides
    @FilterGymScope
    fun provideFilterGymListAdapter(fragment: FilterGymFragment, filterManager: FilterManager): FilterGymListAdapter {
        return FilterGymListAdapter(fragment.activity, filterManager)
    }

    @Provides
    @FilterGymScope
    fun provideItemDecoration(app: Application): RecyclerView.ItemDecoration = SimpleDividerItemDecoration(app)
}