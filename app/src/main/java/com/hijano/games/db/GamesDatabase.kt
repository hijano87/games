package com.hijano.games.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        CoverEntity::class,
        GameEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1
)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
}
