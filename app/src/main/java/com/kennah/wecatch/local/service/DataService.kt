package com.kennah.wecatch.local.service

import com.kennah.wecatch.local.model.WeCatch

interface DataService {
    fun getPokemonList(ne: Array<Double>, sw: Array<Double>): WeCatch
    fun getRarePokemonList(): WeCatch
}