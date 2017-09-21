package com.kennah.wecatch.local.map

import android.view.View
import com.google.android.gms.maps.model.LatLngBounds
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon

interface MapProvider {

    fun getMapView(): View
    fun onCreate()
    fun onResume()
    fun onPause()
    fun onDestroy()
    fun addCachedPokemon(pokemonList: List<Pokemon>)
    fun setCallback(onMapReadyCallback: () -> Unit, onPokemonFoundCallback: () -> Unit)
    fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>)
    fun doExpireCheck()
    fun getBounds(): LatLngBounds
    fun getZoom(): Float
}