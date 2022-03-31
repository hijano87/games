package com.hijano.games.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [
        GameEntity::class,
        RemoteKeysEntity::class
    ],
    version = 3
)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun gamesDao(): GamesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}
