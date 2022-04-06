package com.hijano.games.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hijano.games.api.GamesService
import com.hijano.games.api.getCoverForGame
import com.hijano.games.api.getGameById
import com.hijano.games.db.GameEntity
import com.hijano.games.db.GamesDatabase
import com.hijano.games.db.toGame
import com.hijano.games.di.IoDispatcher
import com.hijano.games.model.Game
import com.hijano.games.model.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GamesRepository @Inject constructor(
    private val gamesService: GamesService,
    private val database: GamesDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun getGames(): Flow<PagingData<Game>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GamesRemoteMediator(database, gamesService),
            pagingSourceFactory = { database.gamesDao().getGames() }
        ).flow.map { pagingData -> pagingData.map(GameEntity::toGame) }
    }

    fun getGameById(id: Long): Flow<Resource<Game>> = flow {
        emit(Resource.Loading)
        val databaseGame = database.gamesDao().getGameById(id).map { it.toGame() }.firstOrNull()
        databaseGame?.let { emit(Resource.Success(it)) }

        try {
            gamesService.getGameById(id)?.let { game ->
                val cover = gamesService.getCoverForGame(id)
                database.gamesDao().insert(
                    GameEntity(
                        game.id,
                        game.name,
                        cover?.imageId,
                        game.storyline,
                        game.summary
                    )
                )
            }
        } catch (throwable: Throwable) {
            emit(Resource.Error)
        }

        emitAll(
            database.gamesDao().getGameById(id)
                .distinctUntilChanged()
                .map { Resource.Success(it.toGame()) }
        )
    }.flowOn(dispatcher)

    companion object {
        private const val PAGE_SIZE = 30
    }
}
