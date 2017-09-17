package com.kennah.wecatch.local

import com.kennah.wecatch.core.utils.LogUtils
import javax.inject.Inject

class FilterManager @Inject constructor(private val preference: Prefs) {

    private var mGymFilter = mutableSetOf<String>()
    private var mPokemonFilter = mutableSetOf<String>()
    private var mPokemonNotifyFilter = mutableSetOf<String>()

    init {

        val preferenceGYM = preference.getGymFilter()
        val preferencePokemon = preference.getPokemonFilter()
        val preferencePokemonNotify = preference.getPokemonNotifyFilter()

        if (!preferenceGYM.isEmpty()) mGymFilter.addAll(preferenceGYM.split(","))
        if (!preferencePokemon.isEmpty()) mPokemonFilter.addAll(preferencePokemon.split(","))
        if (!preferencePokemonNotify.isEmpty()) mPokemonNotifyFilter.addAll(preferencePokemonNotify.split(","))
    }

    fun inGymFilter(type: Int): Boolean = mGymFilter.contains(type.toString())

    fun inPokemonFilter(number: Int, notify: Boolean = false): Boolean {
        if (!notify) return mPokemonFilter.contains(number.toString())
        return mPokemonNotifyFilter.contains(number.toString())
    }

    fun removeFromGymFilter(type: Int) {
        mGymFilter.remove(type.toString())
    }

    fun removeAllGymFilter() {
        mGymFilter.clear()
    }

    fun addAllGymFilter() {
        mGymFilter.clear()
        mGymFilter.addAll(listOf(Constant.GYM).map { it.toString() })
    }

    fun addToGymFilter(type: Int) {
        mGymFilter.add(type.toString())
    }

    fun removeFromPokemonFilter(number: Int, notify: Boolean = false) {
        if (!notify) {
            mPokemonFilter.remove(number.toString())
        } else {
            mPokemonNotifyFilter.remove(number.toString())
        }
    }

    fun removeAllPokemonFilter(generation: Int, notify: Boolean = false) {

        val pokemonFromGeneration = Constant.GENERATION[generation - 1]

        if (!notify && mPokemonFilter.isNotEmpty()) {
            mPokemonFilter.removeAll {
                !it.isBlank() && pokemonFromGeneration.contains(it.toInt())
            }
        } else if (notify && mPokemonNotifyFilter.isNotEmpty()) {
            mPokemonNotifyFilter.removeAll {
                !it.isBlank() && pokemonFromGeneration.contains(it.toInt())
            }
        }
    }

    fun addAllPokemonFilter(generation: Int, notify: Boolean = false) {

        val pokemonFromGeneration = Constant.GENERATION[generation - 1]

        removeAllPokemonFilter(generation, notify)
        pokemonFromGeneration.forEach { addToPokemonFilter(it, notify) }
    }

    fun addToPokemonFilter(number: Int, notify: Boolean = false) {

        LogUtils.debug("DEBUG#FilterManager", "number=[$number]")
        if (!notify) {
            mPokemonFilter.add(number.toString())
        } else {
            mPokemonNotifyFilter.add(number.toString())
        }
    }

    fun save() {

        LogUtils.debug("DEBUG", "POKEMON=[${mPokemonFilter.joinToString(",")}]")
        LogUtils.debug("DEBUG", "POKEMON.NOTIFY=[${mPokemonNotifyFilter.joinToString(",")}]")

        mGymFilter.removeAll { it.isBlank() }
        mPokemonFilter.removeAll { it.isBlank() }
        mPokemonNotifyFilter.removeAll { it.isBlank() }

        preference.getSharedPreferences().edit()
                .putString(Prefs.GYM_FILTER, mGymFilter.joinToString(","))
                .putString(Prefs.POKEMON_FILTER, mPokemonFilter.joinToString(","))
                .putString(Prefs.POKEMON_NOTIFY_FILTER, mPokemonNotifyFilter.joinToString(","))
                .apply()
    }
}