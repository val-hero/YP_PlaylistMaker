package com.example.playlistmaker.core.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.repository.TrackRepository
import com.example.playlistmaker.core.utility.Resource
import kotlinx.coroutines.flow.Flow

class Search(private val trackRepositoryRemote: TrackRepository) {

    operator fun invoke(expression: String): Flow<Resource<List<Track>>> {
        return trackRepositoryRemote.getByTerm(expression)
    }
}