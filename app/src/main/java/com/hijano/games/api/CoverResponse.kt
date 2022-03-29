package com.hijano.games.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverResponse(
    val id: Long,
    val game: Long,
    @Json(name = "image_id") val imageId: String
)
