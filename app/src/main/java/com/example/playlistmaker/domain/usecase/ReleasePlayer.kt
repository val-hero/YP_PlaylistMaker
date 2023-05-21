package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.repository.MediaPlayer

class ReleasePlayer(private val mediaPlayer: MediaPlayer) {
    operator fun invoke() {
        mediaPlayer.release()
    }
}