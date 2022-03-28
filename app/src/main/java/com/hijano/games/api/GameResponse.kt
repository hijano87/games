package com.hijano.games.api

import com.hijano.games.model.Game
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameResponse(
    val id: Long,
    val name: String,
    val cover: Long?
)

fun GameResponse.toGame(imageUrl: String? = null): Game = Game(
    id = id,
    name = name,
    imageUrl = imageUrl
)
