package com.example.playlistmaker.library.playlists.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetTracksInPlaylist(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(trackIds: ArrayList<Long>): Flow<List<Track>> =
        playlistRepository.getAllTracks(trackIds)
}