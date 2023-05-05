package com.silverorange.videoplayer.di

import com.silverorange.videoplayer.network.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideService(): RetrofitService {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        return Retrofit.Builder()
            .baseUrl("http://10.0.0.60:4000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(RetrofitService::class.java)
    }
}