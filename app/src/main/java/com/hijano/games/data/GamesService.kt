package com.hijano.games.data

import retrofit2.http.Body
import retrofit2.http.POST

interface GamesService {
    @POST("/v4/games")
    suspend fun getGames(@Body query: String): List<GameResponse>
}
