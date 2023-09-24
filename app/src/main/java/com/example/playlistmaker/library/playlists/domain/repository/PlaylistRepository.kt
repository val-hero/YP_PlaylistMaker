package com.example.playlistmaker.library.playlists.domain.repository

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun save(playlist: Playlist)

    suspend fun saveToPlaylist(playlist: Playlist, track: Track): Boolean

    suspend fun getById(id: Long): Playlist

    suspend fun getAllTracks(trackIds: List<Long>): Flow<List<Track>>

    suspend fun delete(playlistId: Long)

    suspend fun deleteTrack(playlist: Playlist, trackId: Long)

    suspend fun update(playlist: Playlist)

    fun getAll(): Flow<List<Playlist>>
}