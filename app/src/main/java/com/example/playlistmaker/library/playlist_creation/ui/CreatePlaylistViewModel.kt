package com.example.playlistmaker.library.playlist_creation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.SavePlaylist
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylist
) : ViewModel() {

    fun createPlaylist(name: String, description: String, image: String, onFinish: () -> Unit) {
        viewModelScope.launch {
            savePlaylistUseCase(
                Playlist(name = name, description = description, image = image)
            )
            onFinish()
        }
    }
}