package com.hijano.games.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoversDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(covers: List<CoverEntity>)

    @Query("DELETE FROM covers")
    suspend fun clearCovers()
}