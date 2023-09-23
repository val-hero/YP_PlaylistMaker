package com.example.playlistmaker.library.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val getPlaylistsUseCase: GetPlaylists
) : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    init {
        fetchPlaylists()
    }

    fun fetchPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCase().collect {
                _playlists.postValue(it)
            }
        }
    }
}