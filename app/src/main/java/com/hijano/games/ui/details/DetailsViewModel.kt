package com.hijano.games.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hijano.games.data.GamesRepository
import com.hijano.games.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    repository: GamesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = DetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val game = repository.getGameById(args.gameId)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            Resource.Loading
        )
}
