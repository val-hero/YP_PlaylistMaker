package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepositoryRemote {
    suspend fun getTracks(
        expression: CharSequence,
    ): Flow<Result<List<Track>>>
}