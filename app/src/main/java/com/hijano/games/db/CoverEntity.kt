package com.hijano.games.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "covers")
class CoverEntity(
    @PrimaryKey
    val id: Long,
    val image: String
)