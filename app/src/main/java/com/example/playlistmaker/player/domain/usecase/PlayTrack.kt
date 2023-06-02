package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class PlayTrack(private val mediaPlayerRepository: MediaPlayerRepository) {

    operator fun invoke() {
        mediaPlayerRepository.play()
    }
}