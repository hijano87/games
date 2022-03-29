package com.hijano.games.api

import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.apicalypse.Sort
import retrofit2.http.Body
import retrofit2.http.POST

interface GamesService {
    @POST("/v4/games")
    suspend fun getGames(@Body query: String): List<GameResponse>

    @POST("/v4/covers")
    suspend fun getCovers(@Body query: String): List<CoverResponse>
}

suspend fun GamesService.getGames(page: Int, pageSize: Int): List<GameResponse> {
    return getGames(
        APICalypse()
            .fields("id,name")
            .limit(pageSize)
            .offset(page * pageSize)
            .sort("id", Sort.DESCENDING)
            .buildQuery()
    )
}

suspend fun GamesService.getCovers(ids: List<Long>): List<CoverResponse> {
    return getCovers(
        APICalypse()
            .fields("id,game,image_id")
            .where("game = ${ids.joinToString(prefix = "(", postfix = ")")}")
            .buildQuery()
    )
}
