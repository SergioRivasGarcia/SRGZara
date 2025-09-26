package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName
import com.srg.domain.zara.entity.Origin

data class OriginResponse(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
)

fun OriginResponse.toEntity(): Origin =
    Origin(
        name = name,
        url = url,
    )