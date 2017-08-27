package com.kennah.wecatch.core.di.module

import android.content.Context
import com.kennah.wecatch.BuildConfig
import com.kennah.wecatch.core.di.AppScope
import com.kennah.wecatch.local.API
import com.kennah.wecatch.local.service.DataService
import com.kennah.wecatch.local.service.WeCatchService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @AppScope
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @AppScope
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) client.addInterceptor(httpLoggingInterceptor)
        return client.build()
    }

    @Provides
    @AppScope
    fun provideConvertFactory(): Converter.Factory = JacksonConverterFactory.create()

    @Provides
    @AppScope
    fun provideRetrofit(@Named("server") server: String, okHttpClient: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(server)
            addConverterFactory(factory)
            client(okHttpClient)
        }.build()
    }

    @Provides
    @AppScope
    fun provideApi(retrofit: Retrofit): API = retrofit.create(API::class.java)

    @Provides
    @AppScope
    fun provideDataService(context: Context, api: API): DataService = WeCatchService(context, api)
}