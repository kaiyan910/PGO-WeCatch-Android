package com.kennah.wecatch.module.filter.di.module

import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.module.filter.di.FilterPokemonScope
import com.kennah.wecatch.module.filter.ui.adapter.FilterPokemonGridAdapter
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment
import dagger.Module
import dagger.Provides

@Module
class FilterPokemonFragmentModule {

    @Provides
    @FilterPokemonScope
    fun provideFilterPokemonGridAdapter(fragment: FilterPokemonFragment, filterManager: FilterManager): FilterPokemonGridAdapter {
        return FilterPokemonGridAdapter(fragment.activity, filterManager)
    }
}