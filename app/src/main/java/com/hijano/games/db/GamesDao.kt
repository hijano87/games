package com.hijano.games.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity)

    @Query("SELECT * FROM games ORDER BY id DESC")
    fun getGames(): PagingSource<Int, GameEntity>

    @Query("SELECT * FROM games WHERE id == :id")
    fun getGameById(id: Long): Flow<GameEntity>

    @Query("DELETE FROM games")
    suspend fun clearGames()
}
