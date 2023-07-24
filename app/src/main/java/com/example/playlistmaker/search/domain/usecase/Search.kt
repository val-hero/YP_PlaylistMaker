package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.utility.Result
import kotlinx.coroutines.flow.Flow

class Search(private val trackRepositoryRemote: TrackRepositoryRemote) {

    suspend operator fun invoke(expression: CharSequence): Flow<Result<List<Track>>> {
        return trackRepositoryRemote.getTracks(expression)
    }
}