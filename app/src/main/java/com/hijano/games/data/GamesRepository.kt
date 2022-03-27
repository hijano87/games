package com.hijano.games.data

import com.api.igdb.apicalypse.APICalypse
import com.hijano.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepository @Inject constructor(private val gamesService: GamesService) {

    companion object {
        private val games_query = APICalypse()
            .fields("id,name,cover")
            .buildQuery()
    }

    fun getGames(): Flow<List<Game>> = flow {
        emit(gamesService.getGames(games_query).map { it.toGame() })
    }
}
