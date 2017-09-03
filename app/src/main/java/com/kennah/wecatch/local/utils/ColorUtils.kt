package com.kennah.wecatch.local.utils

object ColorUtils {

    fun getPokemonColor(iv: Int): Int = when(iv) {
        in 80..89 -> 0xFF006DF0.toInt()
        in 90..99 -> 0xFF91DC5A.toInt()
        100 -> 0xFFD80027.toInt()
        else -> 0xFF000000.toInt()
    }
}