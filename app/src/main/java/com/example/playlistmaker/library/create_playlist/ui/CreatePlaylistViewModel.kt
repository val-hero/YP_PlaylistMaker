package com.example.playlistmaker.library.create_playlist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.create_playlist.domain.usecase.SavePlaylist
import com.example.playlistmaker.library.playlists.domain.model.Playlist
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