package com.example.playlistmaker.core.utility

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(val errorType: ErrorType) : Resource<Nothing>()
}