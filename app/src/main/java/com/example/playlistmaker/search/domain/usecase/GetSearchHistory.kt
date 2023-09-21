package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.repository.SearchRepository

class GetSearchHistory(private val searchRepository: SearchRepository) {

    suspend operator fun invoke() = searchRepository.getHistory()
}