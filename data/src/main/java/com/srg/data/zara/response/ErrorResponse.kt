package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val error: String
)