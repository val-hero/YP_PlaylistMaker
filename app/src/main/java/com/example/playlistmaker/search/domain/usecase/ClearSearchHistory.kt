package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.repository.SearchRepository

class ClearSearchHistory(private val searchRepository: SearchRepository) {

    operator fun invoke() {
        searchRepository.clearHistory()
    }
}