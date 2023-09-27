package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository

class GetPlaylistDetails(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(id: Long) = playlistRepository.getById(id)
}