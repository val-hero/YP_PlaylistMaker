package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class ReleasePlayer(private val mediaPlayerRepository: MediaPlayerRepository) {
    operator fun invoke() {
        mediaPlayerRepository.release()
    }
}