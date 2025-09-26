package com.srg.data.zara.response

import com.google.gson.annotations.SerializedName
import com.srg.domain.zara.entity.Character

data class ResultResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("type") val type: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("origin") val origin: OriginResponse,
    @SerializedName("location") val location: LocationResponse,
    @SerializedName("image") val image: String,
    @SerializedName("episode") val episode: List<String>,
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String,
)

fun ResultResponse.toEntity(): Character =
    Character(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        origin = origin.toEntity(),
        location = location.toEntity(),
        image = image,
        episode = episode,
        url = url,
        created = created,
    )