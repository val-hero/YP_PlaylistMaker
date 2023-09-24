package com.example.playlistmaker.library.playlists.domain.repository

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun save(playlist: Playlist)

    suspend fun saveToPlaylist(playlist: Playlist, track: Track): Boolean

    suspend fun getById(id: Long): Playlist

    fun getAll(): Flow<List<Playlist>>
}