package com.srg.data.zara.remote

import com.srg.data.zara.network.BaseApiService
import com.srg.data.zara.response.CharResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharsApi : BaseApiService {

    @GET("character")
    suspend fun getChars(@Query("page") page: Int?): Response<CharResponse>

    @GET("character/")
    suspend fun searchCharByName(
        @Query("page") page: Int?,
        @Query("name") name: String?
    ): Response<CharResponse>

}