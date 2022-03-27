package com.hijano.games.data

import com.hijano.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepository @Inject constructor(private val gamesService: GamesService) {

    companion object {
        const val QUERY = "f id,name,cover;"
    }

    fun getGames(): Flow<List<Game>> = flow {
        emit(gamesService.getGames(QUERY).map { it.toGame() })
    }
}
