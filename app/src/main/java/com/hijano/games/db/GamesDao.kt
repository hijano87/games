package com.hijano.games.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Query("SELECT * FROM games")
    fun getGames(): PagingSource<Int, GameEntity>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
