package com.srg.domain.zara.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String,
) : Parcelable

