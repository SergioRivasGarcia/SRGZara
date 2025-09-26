package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName
import com.srg.domain.zara.entity.CharList

data class CharResponse(
    @SerializedName("info")
    val info: InfoResponse,
    @SerializedName("results")
    val results: List<ResultResponse>,
)

fun CharResponse.toEntity(): CharList =
    CharList(
        info = info.toEntity(),
        characters = results.map { result -> result.toEntity() }
    )