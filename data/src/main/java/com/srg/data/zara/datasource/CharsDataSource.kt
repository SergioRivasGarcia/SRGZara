package com.srg.data.zara.datasource

import com.google.gson.Gson
import com.srg.data.zara.remote.CharsApi
import com.srg.data.zara.response.CharResponse
import com.srg.data.zara.response.ErrorResponse
import com.srg.data.zara.response.toEntity
import com.srg.domain.base.DataResult
import com.srg.domain.zara.entity.CharList
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException

class CharsDataSource(
    private val charsApi: CharsApi,
) : ICharsRemoteDataSource {

    override suspend fun getCharList(page: Int?): DataResult<CharList> {
        val response: Response<CharResponse>

        return try {
            response = charsApi.getChars(page)
            if (response.isSuccessful) {
                DataResult.Success(response.body()?.toEntity())
            } else {
                getGatewayApiErrorResult(response)
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    override suspend fun searchCharByName(page: Int?, query: String?): DataResult<CharList> {
        val response: Response<CharResponse>

        return try {
            response = charsApi.searchCharByName(page, query)
            if (response.isSuccessful) {
                DataResult.Success(response.body()?.toEntity())
            } else {
                getGatewayApiErrorResult(response)
            }
        } catch (e: Exception) {
            DataResult.Error(e)
        }
    }

    private fun getGatewayApiErrorResult(response: Response<*>): DataResult.Error {
        var exception: Exception? = null
        val statusCode = response.code()
        response.errorBody()?.let { body ->
            try {
                exception = Exception(
                    getErrorMessages(body)
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (exception == null) {
            exception = Exception("Unknown Error Occurred")
        } else {
            if (statusCode == GATEWAY_TIMEOUT_STATUS) {
                exception = ConnectException()
            }
        }
        return DataResult.Error(exception)
    }

    private fun getErrorMessages(errorBody: ResponseBody): String {
        val responseBodyString = errorBody.string()
        return try {
            Gson().fromJson(responseBodyString, ErrorResponse::class.java).error
        } catch (ex: Exception) {
            ex.printStackTrace()
            "Unknown Error Occurred"
        }
    }

    companion object {
        private const val GATEWAY_TIMEOUT_STATUS = 504 // Error if cache doesn't work and no net
    }
}