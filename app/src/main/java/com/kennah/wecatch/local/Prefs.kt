package com.kennah.wecatch.local

import android.content.SharedPreferences
import javax.inject.Inject

class Prefs @Inject constructor(private val sharedPreference: SharedPreferences) {

    companion object {
        @JvmStatic
        val NOTIFY_DISTANCE = "notify_distance"
        @JvmStatic
        val NOTIFY_DELAY = "notify_delay"
        @JvmStatic
        val NOTIFY_SWITCH = "notify_switch"
        @JvmStatic
        val GYM_FILTER = "gym_filter"
        @JvmStatic
        val POKEMON_FILTER = "pokemon_filter"
        @JvmStatic
        val POKEMON_NOTIFY_FILTER = "pokemon_notify_filter"
        @JvmStatic
        val MAP_PROVIDER ="map_provider"
    }

    fun getNotifyDistance(): Int = sharedPreference.getInt(NOTIFY_DISTANCE, 2)
    fun setNotifyDistance(value: Int) = sharedPreference.edit()
            .putInt(NOTIFY_DISTANCE, value)
            .apply()

    fun getNotifyDelay(): Int = sharedPreference.getInt(NOTIFY_DELAY, 5)
    fun setNotifyDelay(value: Int) = sharedPreference.edit()
            .putInt(NOTIFY_DELAY, value)
            .apply()

    fun getNotifySwitch(): Boolean = sharedPreference.getBoolean(NOTIFY_SWITCH, false)
    fun setNotifySwitch(value: Boolean) = sharedPreference.edit()
            .putBoolean(NOTIFY_SWITCH, value)
            .apply()

    fun getGymFilter(): String = sharedPreference.getString(GYM_FILTER, "")
    fun setGymFilter(value: String) = sharedPreference.edit()
            .putString(GYM_FILTER, value)
            .apply()


    fun getPokemonFilter(): String = sharedPreference.getString(POKEMON_FILTER, "")
    fun setPokemonFilter(value: String) = sharedPreference.edit()
            .putString(POKEMON_FILTER, value)
            .apply()

    fun getPokemonNotifyFilter(): String = sharedPreference.getString(POKEMON_NOTIFY_FILTER, "")
    fun setPokemonNotifyFilter(value: String) = sharedPreference.edit()
            .putString(POKEMON_NOTIFY_FILTER, value)
            .apply()

    fun getMapProvider(): String = sharedPreference.getString(MAP_PROVIDER, Constant.MAP_PROVIDER_GOOGLE)
    fun setMapProvider(value: String) = sharedPreference.edit()
            .putString(MAP_PROVIDER, value)
            .apply()

    fun getSharedPreferences(): SharedPreferences = sharedPreference
}