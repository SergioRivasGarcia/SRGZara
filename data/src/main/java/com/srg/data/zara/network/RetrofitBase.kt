package com.srg.data.zara.network

import com.google.gson.Gson
import com.srg.data.BuildConfig

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitBase @Inject constructor(
    okHttpClient: OkHttpClient,
    gson: Gson,
) {

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

}