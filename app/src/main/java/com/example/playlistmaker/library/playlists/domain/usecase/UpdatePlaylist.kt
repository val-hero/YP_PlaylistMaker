package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class UpdatePlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlist: Playlist) = playlistRepository.update(playlist)
}