package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName
import com.srg.domain.zara.entity.Location

data class LocationResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)

fun LocationResponse.toEntity(): Location =
    Location(
        name = name,
        url = url,
    )