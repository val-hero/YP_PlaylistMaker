package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.domain.model.Track

class PrepareTrack(private val mediaPlayerRepository: MediaPlayerRepository) {

    operator fun invoke(track: Track) {
        mediaPlayerRepository.prepare(track)
    }
}