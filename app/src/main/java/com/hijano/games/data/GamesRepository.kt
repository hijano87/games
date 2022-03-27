package com.hijano.games.data

import com.hijano.games.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GamesRepository {
    fun getGames(): Flow<List<Game>> = flow {
        emit(
            listOf(
                Game(1L, "","Game1"),
                Game(2L, "","Game2"),
                Game(3L, "","Game3"),
                Game(4L, "","Game4"),
                Game(5L, "","Game5"),
                Game(6L, "","Game6"),
                Game(7L, "","Game7"),
                Game(8L, "","Game8"),
                Game(9L, "","Game9"),
                Game(10L, "","Game10"),
                Game(11L, "","Game11"),
                Game(12L, "","Game12"),
                Game(13L, "","Game13"),
                Game(14L, "","Game14"),
                Game(15L, "","Game15"),
                Game(16L, "","Game16"),
                Game(17L, "","Game17"),
                Game(18L, "","Game18"),
                Game(19L, "","Game19"),
                Game(20L, "","Game20"),
            )
        )
    }
}
