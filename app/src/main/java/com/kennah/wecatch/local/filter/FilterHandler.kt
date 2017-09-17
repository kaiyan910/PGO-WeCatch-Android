package com.kennah.wecatch.local.filter

interface FilterHandler {

    fun isFiltered(pokemonId: Int): Boolean
    fun addAll(generation: Int)
    fun removeAll(generation: Int)
    fun add(pokemon: Int)
    fun remove(pokemon: Int)
}