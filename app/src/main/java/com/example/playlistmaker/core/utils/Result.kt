package com.example.playlistmaker.core.utils

import kotlinx.coroutines.flow.Flow

typealias FlowResult<T> = Flow<Result<T>>
typealias FlowResults<T> = Flow<Result<List<T>>>

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorType: ErrorType) : Result<Nothing>()
}