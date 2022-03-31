package com.hijano.games.data

import androidx.paging.*
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
import kotlinx.coroutines.flow.*
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
        ).flow.map { pagingData ->
            pagingData.map { gameEntity ->
                Game(gameEntity.id, gameEntity.name, gameEntity.image, gameEntity.storyline, gameEntity.summary)
            }
        }
    }

    fun getGameById(id: Long) : Flow<Resource<Game>> = flow {
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
            if (databaseGame == null) emit(Resource.Error)
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
