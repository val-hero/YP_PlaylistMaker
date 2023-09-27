package com.example.playlistmaker.library.playlist_creation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylistDetails
import com.example.playlistmaker.library.playlists.domain.usecase.SavePlaylist
import com.example.playlistmaker.library.playlists.domain.usecase.UpdatePlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val savePlaylistUseCase: SavePlaylist,
    private val getPlaylistDetailsUseCase: GetPlaylistDetails,
    private val updatePlaylistUseCase: UpdatePlaylist
) : ViewModel() {
    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = _playlist

    fun createPlaylist(name: String, description: String, image: String, onFinish: () -> Unit) {
        viewModelScope.launch {
            val playlist = Playlist(name = name, description = description, image = image)
            savePlaylistUseCase(playlist)
            onFinish()
        }
    }

    fun getPlaylistDetails(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlist.postValue(getPlaylistDetailsUseCase(playlistId))
        }
    }

    fun updatePlaylist(name: String, description: String, image: String, onFinish: () -> Unit) {
        viewModelScope.launch {
            _playlist.value?.let { playlist ->
                updatePlaylistUseCase(
                    playlist.copy(name = name, description = description, image = image)
                )
            }
            onFinish()
        }
    }
}