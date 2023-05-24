package com.example.playlistmaker.utility

sealed class Result<out T> {
    data class Success<out T>(val data: T): Result<T>()
    data class Error(val message: String): Result<Nothing>()
    data class NotFound(val message: String): Result<Nothing>()
}