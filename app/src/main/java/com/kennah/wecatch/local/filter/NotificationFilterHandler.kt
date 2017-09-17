package com.kennah.wecatch.local.filter

import com.kennah.wecatch.local.FilterManager

class NotificationFilterHandler(private val filterManager: FilterManager): FilterHandler {

    override fun isFiltered(pokemonId: Int): Boolean = filterManager.inPokemonFilter(pokemonId, true)

    override fun addAll(generation: Int) = filterManager.addAllPokemonFilter(generation, true)

    override fun removeAll(generation: Int) = filterManager.removeAllPokemonFilter(generation, true)

    override fun add(pokemon: Int) = filterManager.addToPokemonFilter(pokemon, true)

    override fun remove(pokemon: Int) = filterManager.removeFromPokemonFilter(pokemon, true)
}