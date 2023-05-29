package com.example.playlistmaker.domain.usecase.search

import com.example.playlistmaker.domain.repository.TrackRepository

class ClearSearchHistory(private val trackRepository: TrackRepository) {

    operator fun invoke() {
        trackRepository.clear()
    }
}