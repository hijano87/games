package com.hijano.games.model

data class Game(
    val id: Long,
    val name: String,
    val imageId: String?,
    val storyline: String?,
    val summary: String?
)
