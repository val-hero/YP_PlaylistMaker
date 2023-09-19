package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.search.domain.repository.SearchRepository

class SaveToHistory(private val searchRepository: SearchRepository) {

    suspend operator fun invoke(track: Track) = searchRepository.saveToHistory(track)
}