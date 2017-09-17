package com.kennah.wecatch.local

import com.kennah.wecatch.local.model.WeCatch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface API {

    @GET("/md?pokemon=true&gyms=true&rares=false")
    @Headers("X-Requested-With: XMLHttpRequest", "Referer: https://www.wecatch.net/")
    fun getPokemonList(@Query("f") token: String,
                       @Query("w") swLat: String,
                       @Query("x") swLng: String,
                       @Query("y") neLat: String,
                       @Query("z") neLng: String): Call<WeCatch>


    @GET("/md?pokemon=true&gyms=false&rares=false")
    @Headers("X-Requested-With: XMLHttpRequest", "Referer: https://www.wecatch.net/")
    fun getPokemonLWithoutGym(@Query("f") token: String,
                       @Query("w") swLat: String,
                       @Query("x") swLng: String,
                       @Query("y") neLat: String,
                       @Query("z") neLng: String): Call<WeCatch>

    @GET("/md?pokemon=true&gyms=true&rares=true")
    @Headers("X-Requested-With: XMLHttpRequest", "Referer: https://www.wecatch.net/")
    fun getRareList(@Query("f") token: String,
                    @Query("w") swLat: String,
                    @Query("x") swLng: String,
                    @Query("y") neLat: String,
                    @Query("z") neLng: String): Call<WeCatch>
}