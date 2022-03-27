package com.hijano.games.data

import com.hijano.games.model.Game
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameResponse(
    val id: Long,
    val name: String,
    val cover: Long?
) {
    fun toGame() = Game(id, cover?.toString().orEmpty(), name)
}
