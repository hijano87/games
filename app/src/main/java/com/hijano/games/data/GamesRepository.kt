package com.hijano.games.data

import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.hijano.games.api.GamesService
import com.hijano.games.api.getCovers
import com.hijano.games.api.toGame
import com.hijano.games.api.toImageUrl
import com.hijano.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GamesRepository @Inject constructor(private val gamesService: GamesService) {

    companion object {
        private val games_query = APICalypse()
            .fields("id,name,cover")
            .buildQuery()

        val gameImageSize = ImageSize.THUMB
        val gameImageType = ImageType.WEBP
    }

    private val noImageUrl = imageBuilder("nocover", gameImageSize, gameImageType)

    fun getGames(): Flow<List<Game>> = flow {
        val games = gamesService.getGames(games_query)
        emit(games.map { it.toGame() })
        val covers = gamesService.getCovers(games.mapNotNull { it.cover })
        val gamesWithImages = games.map { game ->
            val cover = covers.find { cover ->
                game.cover == cover.id
            }
            game.toGame(cover?.toImageUrl(gameImageSize, gameImageType) ?: noImageUrl)
        }
        emit(gamesWithImages)
    }
}
