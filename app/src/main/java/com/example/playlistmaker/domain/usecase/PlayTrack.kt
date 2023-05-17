package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.repository.MediaPlayer

class PlayTrack(private val mediaPlayer: MediaPlayer) {

    operator fun invoke() {
        mediaPlayer.play()
    }
}