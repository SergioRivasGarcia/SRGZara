package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName
import com.srg.domain.zara.entity.Info

data class InfoResponse(
    @SerializedName("count") val count: Long,
    @SerializedName("pages") val pages: Long,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?,
)

fun InfoResponse.toEntity(): Info =
    Info(
        count = count,
        pages = pages,
        next = next,
        prev = prev,
    )