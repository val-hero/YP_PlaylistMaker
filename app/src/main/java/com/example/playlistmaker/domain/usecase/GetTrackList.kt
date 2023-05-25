package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class GetTrackList(private val trackRepository: TrackRepository) {

    operator fun invoke(): ArrayList<Track> {
       return trackRepository.getTrackList()
    }
}