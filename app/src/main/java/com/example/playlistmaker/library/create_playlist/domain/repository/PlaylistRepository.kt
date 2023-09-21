package com.example.playlistmaker.library.create_playlist.domain.repository

import com.example.playlistmaker.library.create_playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun save(playlist: Playlist)

    fun getDetails(id: Long): Playlist

    fun getAll(): Flow<List<Playlist>>
}