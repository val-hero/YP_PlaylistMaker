package com.example.playlistmaker.library.favourite.domain.repository

import com.example.playlistmaker.core.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun getAll(): Flow<List<Track>>

    suspend fun save(track: Track)

    suspend fun delete(id: Long)

    fun getIds(): Flow<List<Long>>
}