package com.example.playlistmaker.library.create_playlist.domain.usecase

import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class SavePlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlist: Playlist) = playlistRepository.save(playlist)
}