package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository

class GetTrackList(private val trackRepository: TrackRepository) {

    operator fun invoke(): ArrayList<Track> {
       return trackRepository.getTrackList()
    }
}