package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class DeletePlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlistId: Long) = playlistRepository.delete(playlistId)
}