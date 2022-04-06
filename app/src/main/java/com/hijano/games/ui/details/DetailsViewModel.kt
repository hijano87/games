package com.hijano.games.ui.details

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hijano.games.R
import com.hijano.games.data.GamesRepository
import com.hijano.games.model.Resource
import com.hijano.games.ui.ErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: GamesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = DetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val internalUiState = MutableStateFlow(DetailsUiState(isLoading = true))
    val uiState = internalUiState.asStateFlow()

    init {
        refresh()
    }

    private fun refresh() {
        internalUiState.update { currentUiState -> currentUiState.copy(isLoading = true) }
        viewModelScope.launch {
            repository.getGameById(args.gameId).collect { resource ->
                internalUiState.update { currentUiState ->
                    when (resource) {
                        Resource.Error -> currentUiState.copy(
                            errorMessages = currentUiState.errorMessages.addError(R.string.something_went_wrong),
                            isLoading = false
                        )
                        Resource.Loading -> currentUiState.copy(isLoading = true)
                        is Resource.Success -> currentUiState.copy(
                            game = resource.data,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun errorShown(errorId: Long) {
        internalUiState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }

    private fun List<ErrorMessage>.addError(@StringRes newErrorId: Int): List<ErrorMessage> =
        this + ErrorMessage(
            id = UUID.randomUUID().mostSignificantBits,
            messageId = newErrorId
        )
}
