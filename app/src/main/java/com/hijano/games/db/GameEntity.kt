package com.hijano.games.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hijano.games.model.Game

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val image: String?,
    val storyline: String?,
    val summary: String?
)

fun GameEntity.toGame(): Game = Game(id, name, image, storyline, summary)
