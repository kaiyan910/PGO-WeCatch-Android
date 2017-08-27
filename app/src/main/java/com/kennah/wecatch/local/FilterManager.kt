package com.kennah.wecatch.local

import android.content.SharedPreferences
import com.kennah.wecatch.core.utils.LogUtils

class FilterManager(private val preference: SharedPreferences) {

    private val GYM_FILTER: String = "gym_filter"
    private val POKEMON_FILTER: String = "pokemon_filter"

    private var mGymFilter = mutableListOf<String>()
    private var mPokemonFilter = mutableListOf<String>()

    init {
        mGymFilter.addAll(preference.getString(GYM_FILTER, "").split(","))
        mPokemonFilter.addAll(preference.getString(POKEMON_FILTER, "").split(","))
    }

    fun inGymFilter(type: Int): Boolean = mGymFilter.contains(type.toString())

    fun inPokemonFilter(number: Int): Boolean = mPokemonFilter.contains(number.toString())

    fun removeFromGymFilter(type: Int) {
        mGymFilter.remove(type.toString())
    }

    fun removeFromPokemonFilter(number: Int) {
        mPokemonFilter.remove(number.toString())
    }

    fun addToGymFilter(type: Int) {
        mGymFilter.add(type.toString())
    }

    fun addToPokemonFilter(number: Int) {
        mPokemonFilter.add(number.toString())
    }

    fun save() {

        LogUtils.debug(this, mGymFilter.joinToString(","), "")
        LogUtils.debug(this, mPokemonFilter.joinToString(","), "")

        preference.edit()
                .putString(GYM_FILTER, mGymFilter.joinToString(","))
                .putString(POKEMON_FILTER, mPokemonFilter.joinToString(","))
                .apply()
    }
}