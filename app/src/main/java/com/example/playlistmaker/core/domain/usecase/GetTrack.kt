package com.example.playlistmaker.core.domain.usecase

import com.example.playlistmaker.core.domain.repository.TrackRepository

class GetTrack(private val trackRepository: TrackRepository) {

    operator fun invoke(id: Long) = trackRepository.getById(id)
}