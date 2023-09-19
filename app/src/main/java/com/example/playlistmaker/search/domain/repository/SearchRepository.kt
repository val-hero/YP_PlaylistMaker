package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utils.FlowResults
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(query: String): FlowResults<Track>
    suspend fun saveToHistory(track: Track)
    suspend fun getHistory(): Flow<List<Track>>
    fun clearHistory()
}