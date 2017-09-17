package com.kennah.wecatch.local.service

import android.content.Context
import com.kennah.wecatch.core.utils.ServiceUtils
import com.kennah.wecatch.local.API
import com.kennah.wecatch.local.model.WeCatch
import com.kennah.wecatch.local.utils.MathUtils
import com.kennah.wecatch.local.utils.WeCatchUtils
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class WeCatchService @Inject constructor (private val context: Context, private val api:API): DataService {

    @JvmField
    var swHK = arrayOf(22.17296, 113.83215)
    @JvmField
    var neHK = arrayOf(22.570745, 114.392806)

    override fun getRarePokemonList(): WeCatch {

        val rareCenter = getRareCenter(neHK, swHK)

        return ServiceUtils.call<WeCatch>(context) {

            val token = WeCatchUtils.generateToken(rareCenter[0], rareCenter[1])
                    .replace("\n", "")
                    .replace("\r", "")

            val swLat = MathUtils.toDecimal5(swHK[0])
            val swLng = MathUtils.toDecimal5(swHK[1])
            val neLat = MathUtils.toDecimal6(neHK[0])
            val neLng = MathUtils.toDecimal6(neHK[1])

            api.getRareList(token, swLat, swLng, neLat, neLng).execute()
        }
    }

    override fun getPokemonList(ne: Array<Double>, sw: Array<Double>, gym: Boolean): WeCatch {

        val centerLatitude = java.lang.Double.parseDouble(MathUtils.toDecimal5((ne[0] + sw[0]) / 2))
        val centerLongitude = java.lang.Double.parseDouble(MathUtils.toDecimal5((ne[1] + sw[1]) / 2))

        return ServiceUtils.call<WeCatch>(context) {

            val token = WeCatchUtils.generateToken(centerLatitude, centerLongitude)
                    .replace("\n", "")
                    .replace("\r", "")

            val swLat = MathUtils.toDecimal5(sw[0])
            val swLng = MathUtils.toDecimal5(sw[1])
            val neLat = MathUtils.toDecimal6(ne[0])
            val neLng = MathUtils.toDecimal6(ne[1])

            if (gym) {
                api.getPokemonList(token, swLat, swLng, neLat, neLng).execute()
            } else {
                api.getPokemonLWithoutGym(token, swLat, swLng, neLat, neLng).execute()
            }
        }
    }


    private fun getRareCenter(ne: Array<Double>, sw: Array<Double>): Array<Double> {

        val centerLatitude = java.lang.Double.parseDouble(MathUtils.toDecimal5((ne[0] + sw[0]) / 2))
        val centerLongitude = java.lang.Double.parseDouble(MathUtils.toDecimal5((ne[1] + sw[1]) / 2))

        return arrayOf(centerLatitude, centerLongitude)
    }
}