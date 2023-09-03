package com.example.playlistmaker.core.domain.usecase

import com.example.playlistmaker.core.domain.repository.TrackRepository

class GetTrackList(private val trackRepository: TrackRepository) {

    operator fun invoke() = trackRepository.getList()
}