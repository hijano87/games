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

suspend fun GamesService.getGameById(id: Long): GameResponse? {
    return getGames(
        APICalypse()
            .fields("id, name")
            .limit(1)
            .where("id = $id")
            .buildQuery()
    ).firstOrNull()
}

suspend fun GamesService.getCovers(ids: List<Long>): List<CoverResponse> {
    return getCovers(
        APICalypse()
            .fields("id,game,image_id")
            .where("game = ${ids.joinToString(prefix = "(", postfix = ")")}")
            .limit(ids.size)
            .buildQuery()
    )
}

suspend fun GamesService.getCoverForGame(gameId: Long): CoverResponse? {
    return getCovers(
        APICalypse()
            .fields("id,game,image_id")
            .where("game = $gameId")
            .limit(1)
            .buildQuery()
    ).firstOrNull()
}
