package com.kennah.wecatch.module.main.di.module

import android.app.Application
import android.content.Context
import com.kennah.wecatch.core.helper.LocationHelper
import com.kennah.wecatch.local.Constant
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.Prefs
import com.kennah.wecatch.local.map.GoogleMapProvider
import com.kennah.wecatch.local.map.MapProvider
import com.kennah.wecatch.local.map.OpenStreetMapProvider
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.module.main.contract.MainContract
import com.kennah.wecatch.module.main.di.MainFragmentScope
import com.kennah.wecatch.module.main.di.MainServiceScope
import com.kennah.wecatch.module.main.presenter.MainPresenter
import com.kennah.wecatch.module.main.ui.view.FloatingMapView
import com.kennah.wecatch.module.main.ui.view.PokemonMapView
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference

@Module
class MainServiceModule {

    @Provides
    @MainServiceScope
    fun provideLocationHelper(app: Application): LocationHelper = LocationHelper(app)

    @Provides
    @MainServiceScope
    fun provideMainPresenter(service: DataService, filterManager: FilterManager, prefs: Prefs): MainContract.Presenter {
        return MainPresenter(service, filterManager, prefs)
    }

    @Provides
    @MainServiceScope
    fun provideMapProvider(app: Application, locationHelper: LocationHelper, pref: Prefs): MapProvider {

        return when (pref.getMapProvider()) {
            Constant.MAP_PROVIDER_GOOGLE -> GoogleMapProvider(app, locationHelper)
            else -> OpenStreetMapProvider(app, locationHelper, pref.getSharedPreferences())
        }
    }

    @Provides
    @MainServiceScope
    fun providePokemonMapView(app: Context, presenter: MainContract.Presenter, mapProvider: MapProvider): PokemonMapView {
        return PokemonMapView(app, presenter, mapProvider)
    }

    @Provides
    @MainServiceScope
    fun provideFloatingMapView(app: Context, pokemonMapView: PokemonMapView, prefs: Prefs) = FloatingMapView(app, pokemonMapView, prefs)


}