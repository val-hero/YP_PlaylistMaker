package com.example.playlistmaker.library.playlist_details.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.library.playlists.domain.model.Playlist
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylistDetails
import com.example.playlistmaker.library.playlists.domain.usecase.GetTracksInPlaylist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsViewModel(
    private val getPlaylistDetailsUseCase: GetPlaylistDetails,
    private val getTracksInPlaylistUseCase: GetTracksInPlaylist,
    private val saveTrackUseCase: SaveTrack
) : ViewModel() {
    private val _tracksInPlaylist = MutableLiveData<List<Track>>()
    val tracksInPlaylist: LiveData<List<Track>> = _tracksInPlaylist

    private val _playlist = MutableLiveData<Playlist?>()
    val playlist: LiveData<Playlist?> = _playlist

    private val _playlistDuration = MutableLiveData<Int>()
    val playlistDuration: LiveData<Int> = _playlistDuration

    fun fetchPlaylistDetails(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _playlist.postValue(getPlaylistDetailsUseCase(playlistId))
        }
    }

    fun getTracksInPlaylist(tracksIds: List<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            getTracksInPlaylistUseCase(tracksIds).collect {
                _tracksInPlaylist.postValue(it)

                withContext(Dispatchers.Main) {
                    calculateTotalDuration()
                }
            }
        }
    }

    fun saveTrack(track: Track) {
        saveTrackUseCase(track)
    }

    private fun calculateTotalDuration() {
        var totalDuration = 0L
        _tracksInPlaylist.value?.let { tracks ->
            tracks.forEach { totalDuration += it.duration }
        }
        _playlistDuration.value = SimpleDateFormat("mm", Locale.getDefault()).format(totalDuration).toInt()
    }
}