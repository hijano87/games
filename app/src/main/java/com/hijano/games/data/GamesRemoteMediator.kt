package com.hijano.games.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hijano.games.api.GamesService
import com.hijano.games.api.getCovers
import com.hijano.games.api.getGames
import com.hijano.games.db.*

@OptIn(ExperimentalPagingApi::class)
class GamesRemoteMediator(
    private val database: GamesDatabase,
    private val gamesApi: GamesService
) : RemoteMediator<Int, GameEntity>() {
    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GameEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = state.anchorItem?.let { getRemoteKeyForItem(it) }
                (remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX)
            }
            LoadType.PREPEND -> {
                val remoteKeys = state.firstItemOrNull()?.let { getRemoteKeyForItem(it) }
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = state.lastItemOrNull()?.let { getRemoteKeyForItem(it) }
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val games = gamesApi.getGames(page, state.config.pageSize)
            val covers = gamesApi.getCovers(games.map { it.id })
            val endOfPaginationReached = games.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.gamesDao().clearGames()
                    database.remoteKeysDao().clearRemoteKeys()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = games.map {
                    RemoteKeysEntity(gameId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.gamesDao().insertAll(
                    games.map { game ->
                        GameEntity(
                            game.id,
                            game.name,
                            covers.find { cover -> cover.game == game.id }?.imageId,
                            game.summary
                        )
                    }
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private val <Key : Any, Value : Any> PagingState<Key, Value>.anchorItem: Value?
        get() = anchorPosition?.let(::closestItemToPosition)

    private suspend fun getRemoteKeyForItem(game: GameEntity): RemoteKeysEntity? {
        return database.remoteKeysDao().remoteKeysForGameId(game.id)
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
    }
}
