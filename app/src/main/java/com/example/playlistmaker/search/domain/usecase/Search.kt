package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import kotlinx.coroutines.flow.Flow

class Search(private val trackRepositoryRemote: TrackRepositoryRemote) {

    suspend operator fun invoke(expression: CharSequence): Flow<Result<List<Track>>> {
        return trackRepositoryRemote.getTracks(expression)
    }
}