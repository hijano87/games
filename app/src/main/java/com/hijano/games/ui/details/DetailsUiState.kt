package com.hijano.games.ui.details

import com.hijano.games.model.Game
import com.hijano.games.ui.ErrorMessage

data class DetailsUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val game: Game? = null
) {
    val hasDetails get() = game != null
}
