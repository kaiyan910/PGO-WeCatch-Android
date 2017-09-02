package com.kennah.wecatch.local.model

data class Pokemon(
        val pokemonId: Int,
        val latitude: Double,
        val longitude: Double,
        val expireTime: Long,
        val iv: Int,
        val cp: Int,
        val stamina: Int,
        val attack: Int,
        val defense: Int,
        val move1: Int,
        val move2: Int
)