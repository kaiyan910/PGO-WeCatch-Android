package com.kennah.wecatch.module.filter.di.component

import com.kennah.wecatch.module.filter.di.FilterPokemonScope
import com.kennah.wecatch.module.filter.di.module.FilterPokemonFragmentModule
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@FilterPokemonScope
@Subcomponent(modules = arrayOf(FilterPokemonFragmentModule::class))
interface FilterPokemonSubComponent : AndroidInjector<FilterPokemonFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FilterPokemonFragment>()
}