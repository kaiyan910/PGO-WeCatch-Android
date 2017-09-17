package com.kennah.wecatch.module.main.di.module

import android.app.Application
import android.content.Context
import com.kennah.wecatch.core.di.AppScope
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.model.Pokemon
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.di.MainFragmentScope
import com.kennah.wecatch.module.main.presenter.MainPresenter
import com.kennah.wecatch.module.main.ui.fragment.MainFragment
import com.kennah.wecatch.module.main.ui.view.PokemonMapView
import dagger.Module
import dagger.Provides

@Module
class MainFragmentModule {

    @Provides
    @MainFragmentScope
    fun provideLocationHelper(app: Application): LocationHelper = LocationHelper(app)

    @Provides
    @MainFragmentScope
    fun provideMainPresenter(service: DataService, filterManager: FilterManager, pref: Prefs): MainContract.Presenter {
        return MainPresenter(service,filterManager, pref)
    }

    @Provides
    @MainFragmentScope
    fun providePokemonMapView(app: Context, presenter: MainContract.Presenter, locationHelper: LocationHelper): PokemonMapView {
        return PokemonMapView(app, presenter, locationHelper)
    }

}