package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.repository.TrackRepository

class ClearSearchHistory(private val trackRepository: TrackRepository) {

    operator fun invoke() {
        trackRepository.clear()
    }
}