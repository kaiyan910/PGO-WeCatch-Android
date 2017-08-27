package com.kennah.wecatch.module.filter.di.module

import android.support.v4.app.Fragment
import com.kennah.wecatch.module.filter.di.component.FilterGymSubComponent
import com.kennah.wecatch.module.filter.di.component.FilterPokemonSubComponent
import com.kennah.wecatch.module.filter.ui.fragment.FilterGymFragment
import com.kennah.wecatch.module.filter.ui.fragment.FilterPokemonFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(
        FilterGymSubComponent::class,
        FilterPokemonSubComponent::class
))
abstract class FilterBuilderModule {

    @Binds
    @IntoMap
    @FragmentKey(FilterGymFragment::class)
    abstract fun bindFilterGymFragmentInjectorFactory(builder: FilterGymSubComponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @FragmentKey(FilterPokemonFragment::class)
    abstract fun bindFilterPokemonFragmentInjectorFactory(builder: FilterPokemonSubComponent.Builder): AndroidInjector.Factory<out Fragment>
}