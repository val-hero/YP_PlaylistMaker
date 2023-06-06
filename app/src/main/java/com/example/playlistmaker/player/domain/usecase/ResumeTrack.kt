package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class ResumeTrack(private val repository: MediaPlayerRepository) {
    operator fun invoke() {
        repository.resume()
    }
}