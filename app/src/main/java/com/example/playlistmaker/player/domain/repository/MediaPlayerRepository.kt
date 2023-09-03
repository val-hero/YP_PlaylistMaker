package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.player.domain.model.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface MediaPlayerRepository {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun release()
    fun getCurrentState(): StateFlow<PlayerState>
}