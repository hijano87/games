package com.hijano.games.model

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    object Error : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
}
