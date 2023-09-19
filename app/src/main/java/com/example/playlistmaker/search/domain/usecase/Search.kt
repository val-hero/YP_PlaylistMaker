package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow

class Search(private val searchRepository: SearchRepository) {

    suspend operator fun invoke(query: String): Flow<Result<List<Track>>> {
        return searchRepository.search(query)
    }
}