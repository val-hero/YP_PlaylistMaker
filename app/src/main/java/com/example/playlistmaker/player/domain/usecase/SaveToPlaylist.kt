package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class SaveToPlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlist: Playlist, track: Track): Boolean =
        playlistRepository.saveToPlaylist(playlist, track)
}