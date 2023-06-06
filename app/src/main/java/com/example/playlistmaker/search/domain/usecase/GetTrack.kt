package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository

class GetTrack(private val trackRepository: TrackRepository) {

    operator fun invoke(): Track = trackRepository.getTrack()
}