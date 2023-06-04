package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MediaPlayerRepository {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Long?
    fun getCurrentState(): StateFlow<PlayerState>
}