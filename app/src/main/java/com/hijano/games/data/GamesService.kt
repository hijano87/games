package com.hijano.games.data

import com.api.igdb.apicalypse.APICalypse
import retrofit2.http.Body
import retrofit2.http.POST

interface GamesService {
    @POST("/v4/games")
    suspend fun getGames(@Body query: String): List<GameResponse>

    @POST("/v4/covers")
    suspend fun getCovers(@Body query: String): List<CoverResponse>
}

suspend fun GamesService.getCovers(ids: List<Long>): List<CoverResponse> {
    return getCovers(
        APICalypse()
            .fields("id,image_id")
            .where("id = ${ids.joinToString(prefix = "(", postfix = ")")}")
            .buildQuery()
    )
}
