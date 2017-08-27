package com.kennah.wecatch.module.main.presenter

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.kennah.wecatch.core.base.CommonPresenter
import com.kennah.wecatch.local.FilterManager
import com.kennah.wecatch.local.model.ProcessedData
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.local.utils.WeCatchUtils
import com.kennah.wecatch.module.main.contract.MainContract
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import javax.inject.Inject

class MainPresenter @Inject constructor(val view: MainContract.View, private val service: DataService, private val filterManager: FilterManager) :
        CommonPresenter<MainContract.View>(view), MainContract.Presenter {

    override fun getPokemon(bound: LatLngBounds) {

        val ne: Array<Double> = arrayOf(bound.northeast.latitude, bound.northeast.longitude)
        val sw: Array<Double> = arrayOf(bound.southwest.latitude, bound.southwest.longitude)

        async(UI) {

            val response = bg {
                service.getPokemonList(ne, sw)
            }

            val weCatch = response.await()

            val processedData = bg {

                val pokemonList = weCatch.pokemons
                        .map { WeCatchUtils.decode(it) }
                        .filter {
                            bound.contains(LatLng(it.latitude, it.longitude)) &&
                                    !filterManager.inPokemonFilter(it.pokemonId)
                        }

                val gymList = weCatch.gyms
                        .filter {
                            bound.contains(LatLng(it.location[1], it.location[0])) &&
                                    !filterManager.inGymFilter(it.raidLevel)
                        }

                ProcessedData(pokemonList, gymList)
            }

            val (pokemonList, gymList) = processedData.await()

            mView.get()?.onPokemonFound(pokemonList, gymList)

        }.invokeOnCompletion { e -> handleError(e) }
    }
}