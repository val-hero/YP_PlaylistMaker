package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class DeletePlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlist: Playlist) = playlistRepository.deletePlaylist(playlist)
}