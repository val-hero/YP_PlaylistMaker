package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track

interface MediaPlayerRepository {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun release()
    fun getCurrentPosition(): Long
    fun setOnStateChangeListener(callback: (PlayerState) -> Unit)
    fun removeOnStateChangeListener()
}