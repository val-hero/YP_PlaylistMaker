package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track

interface MediaPlayer {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun release()
    fun getCurrentState(): PlayerState
    fun getCurrentPosition(): Long
}