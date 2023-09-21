package com.example.playlistmaker.library.create_playlist.data.repository

import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.library.create_playlist.domain.model.Playlist
import com.example.playlistmaker.library.create_playlist.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(
    database: AppDatabase
) : PlaylistRepository {
    override fun save(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun getDetails(id: Long): Playlist {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Playlist>> {
        TODO("Not yet implemented")
    }
}