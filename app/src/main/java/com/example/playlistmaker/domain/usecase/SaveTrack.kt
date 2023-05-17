package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class SaveTrack(private val trackRepository: TrackRepository) {

    operator fun invoke(track: Track) {
        trackRepository.saveTrack(track)
    }
}