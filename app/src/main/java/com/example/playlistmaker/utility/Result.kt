package com.example.playlistmaker.utility

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val type: ErrorType) : Result<Nothing>()
}