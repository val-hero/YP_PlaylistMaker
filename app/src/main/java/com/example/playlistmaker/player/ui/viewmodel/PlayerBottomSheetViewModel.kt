package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.usecase.GetSelectedTrack
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylists
import com.example.playlistmaker.player.domain.usecase.SaveToPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerBottomSheetViewModel(
    private val getPlaylistsUseCase: GetPlaylists,
    private val saveTrackUseCase: SaveTrack,
    private val saveToPlaylistUseCase: SaveToPlaylist,
    private val getSelectedTrackUseCase: GetSelectedTrack
) : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    init {
        fetchPlaylists()

    }

    private fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCase().collect {
                _playlists.postValue(it)
            }
        }
    }

    fun saveToPlaylist(playlist: Playlist, onFinish: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val track = getSelectedTrackUseCase().apply { this.playlistIds.add(playlist.id) }
            saveTrackUseCase(track)
            val result = saveToPlaylistUseCase(playlist, track)
            withContext(Dispatchers.Main) {
                onFinish(result)
            }
        }
    }
}