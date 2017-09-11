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

class MainPresenter @Inject constructor(private val service: DataService, private val filterManager: FilterManager) :
        CommonPresenter<MainContract.View>(), MainContract.Presenter {

    override fun getPokemon(bound: LatLngBounds, zoom: Float) {

        val ne: Array<Double> = arrayOf(bound.northeast.latitude, bound.northeast.longitude)
        val sw: Array<Double> = arrayOf(bound.southwest.latitude, bound.southwest.longitude)

        async(UI) {

            val rare = when {
                zoom <= 13F -> true
                else -> false
            }

            val response = bg {
                if (rare) {
                    service.getRarePokemonList()
                } else {
                    service.getPokemonList(ne, sw)
                }
            }

            val weCatch = response.await()

            val processedData = bg {

                val pokemonList = weCatch.pokemons
                        .map { WeCatchUtils.decode(it) }
                        .filter {
                            if (rare) {
                                !filterManager.inPokemonFilter(it.pokemonId)
                            } else {
                                bound.contains(LatLng(it.latitude, it.longitude)) &&
                                        !filterManager.inPokemonFilter(it.pokemonId)
                            }
                        }

                val gymList = weCatch.gyms
                        .filter {

                            val raidLevel = it.raidLevel ?: 0
                            val raidPokemonId = it.raidPokemonId ?: 0
                            val location = it.location ?: arrayOf(0.0, 0.0)

                            if (rare) {
                                !filterManager.inGymFilter(raidLevel) && !filterManager.inPokemonFilter(raidPokemonId)
                            } else {
                                bound.contains(LatLng(location[1], location[0])) &&
                                        !filterManager.inGymFilter(raidLevel) &&
                                        !filterManager.inPokemonFilter(raidPokemonId)
                            }
                        }

                ProcessedData(pokemonList, gymList)
            }

            val (pokemonList, gymList) = processedData.await()

            mView.get()?.onPokemonFound(pokemonList, gymList)

        }.invokeOnCompletion { e -> handleError(e) }
    }
}