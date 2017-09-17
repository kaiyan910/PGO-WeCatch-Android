package com.kennah.wecatch.local.filter

import com.kennah.wecatch.core.utils.LogUtils
import com.kennah.wecatch.local.FilterManager

class NormalFilterHandler(private val filterManager: FilterManager): FilterHandler {

    override fun isFiltered(pokemonId: Int): Boolean = filterManager.inPokemonFilter(pokemonId)

    override fun addAll(generation: Int) = filterManager.addAllPokemonFilter(generation)

    override fun removeAll(generation: Int) = filterManager.removeAllPokemonFilter(generation)

    override fun add(pokemon: Int) {

        LogUtils.debug("DEBUG#NormalFilterHandler", "number=[$pokemon]")
        filterManager.addToPokemonFilter(pokemon)
    }

    override fun remove(pokemon: Int) = filterManager.removeFromPokemonFilter(pokemon)

}