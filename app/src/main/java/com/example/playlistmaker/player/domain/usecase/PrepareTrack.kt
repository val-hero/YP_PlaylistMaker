package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class PrepareTrack(private val mediaPlayerRepository: MediaPlayerRepository) {

    operator fun invoke(track: Track) {
        mediaPlayerRepository.prepare(track)
    }
}