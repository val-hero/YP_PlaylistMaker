package com.example.playlistmaker.domain.usecase.player

import com.example.playlistmaker.domain.repository.MediaPlayerRepository

class ReleasePlayer(private val mediaPlayerRepository: MediaPlayerRepository) {
    operator fun invoke() {
        mediaPlayerRepository.release()
    }
}