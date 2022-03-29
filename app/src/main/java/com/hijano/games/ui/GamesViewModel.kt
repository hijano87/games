package com.hijano.games.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hijano.games.data.GamesRepository
import com.hijano.games.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(repository: GamesRepository) : ViewModel() {
    val games: Flow<PagingData<Game>> = repository.getGames().cachedIn(viewModelScope)
}
