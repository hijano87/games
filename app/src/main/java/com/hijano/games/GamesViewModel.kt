package com.hijano.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hijano.games.data.GamesRepository
import com.hijano.games.model.Game
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class GamesViewModel(repository: GamesRepository = GamesRepository()) : ViewModel() {
    val games: StateFlow<List<Game>> = repository.getGames()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
