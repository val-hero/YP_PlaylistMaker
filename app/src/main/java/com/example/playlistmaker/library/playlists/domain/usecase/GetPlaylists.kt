package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow


class GetPlaylists(private val playlistRepository: PlaylistRepository) {

    operator fun invoke(): Flow<List<Playlist>> = playlistRepository.getAll()
}