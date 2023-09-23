package com.example.playlistmaker.library.playlists.domain.repository

import com.example.playlistmaker.library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun save(playlist: Playlist)

    fun getDetails(id: Long): Playlist

    fun getAll(): Flow<List<Playlist>>
}