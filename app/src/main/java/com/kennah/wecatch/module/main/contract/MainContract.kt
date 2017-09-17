package com.kennah.wecatch.module.main.contract

import com.google.android.gms.maps.model.LatLngBounds
import com.kennah.wecatch.core.base.BasePresenter
import com.kennah.wecatch.core.base.BaseView
import com.kennah.wecatch.local.model.Gym
import com.kennah.wecatch.local.model.Pokemon

interface MainContract {

    interface View: BaseView {
        fun onPokemonFound(pokemonList: List<Pokemon>, gymList: List<Gym>)
    }

    interface Presenter: BasePresenter<View> {
        fun getPokemon(bound: LatLngBounds, zoom: Float)
        fun getPokemonWithoutGym(bound: LatLngBounds, zoom: Float, alertList: MutableList<String>)
    }
}