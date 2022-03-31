package com.hijano.games.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameResponse(
    val id: Long,
    val name: String,
    val cover: Long?,
    val storyline: String?,
    val summary: String?
)
