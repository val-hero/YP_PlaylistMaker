package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class SaveTrackList(private val trackRepository: TrackRepository) {

    operator fun invoke(tracks: ArrayList<Track>) {
        trackRepository.saveTrackList(tracks)
    }
}