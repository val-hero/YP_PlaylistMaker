package com.example.playlistmaker.core.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.repository.TrackRepository

class GetSelectedTrack(private val trackRepository: TrackRepository) {

    operator fun invoke(): Track = trackRepository.get()
}