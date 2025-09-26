package com.srg.zara.di

import com.srg.data.zara.remote.CharsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun characters(retrofit: Retrofit): CharsApi =
        retrofit.create(CharsApi::class.java)

}