package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.usecase.GetSelectedTrack
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylists
import com.example.playlistmaker.player.domain.usecase.SaveToPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val getPlaylistsUseCase: GetPlaylists,
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

    fun saveToPlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            saveToPlaylistUseCase(playlist, getSelectedTrackUseCase())
        }
    }
}