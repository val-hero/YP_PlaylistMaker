package com.example.playlistmaker.player.domain.model

import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.search.domain.model.Track

sealed class PlayerState {
    object Default : PlayerState()
    data class Prepared(val track: Track) : PlayerState()
    data class Playing(val playbackTime: Long?) : PlayerState()
    object Paused : PlayerState()
    object Completed : PlayerState()
    data class Error(val errorType: ErrorType) : PlayerState()
}