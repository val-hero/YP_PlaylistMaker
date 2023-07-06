package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utility.Result
import kotlinx.coroutines.flow.Flow


interface TrackRepositoryRemote {

    suspend fun getTracks(expression: CharSequence): Flow<Result<List<Track>>>
}