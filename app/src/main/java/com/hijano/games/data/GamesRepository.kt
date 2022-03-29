package com.hijano.games.data

import androidx.paging.*
import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.hijano.games.api.GamesService
import com.hijano.games.db.GamesDatabase
import com.hijano.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GamesRepository @Inject constructor(
    private val gamesService: GamesService,
    private val database: GamesDatabase
) {
    fun getGames(): Flow<PagingData<Game>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GamesRemoteMediator(database, gamesService),
            pagingSourceFactory = { database.gamesDao().getGames() }
        ).flow.map { pagingData ->
            pagingData.map { gameEntity ->
                Game(gameEntity.id, gameEntity.name, gameEntity.image)
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
