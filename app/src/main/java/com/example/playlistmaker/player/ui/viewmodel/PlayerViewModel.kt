package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.favourite.domain.usecase.CheckFavouriteStatus
import com.example.playlistmaker.library.favourite.domain.usecase.DeleteFromFavourites
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import com.example.playlistmaker.library.favourite.domain.usecase.SaveToFavourites
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.usecase.GetPlayerState
import com.example.playlistmaker.player.domain.usecase.PauseTrack
import com.example.playlistmaker.player.domain.usecase.PlayTrack
import com.example.playlistmaker.player.domain.usecase.PrepareTrack
import com.example.playlistmaker.player.domain.usecase.ReleasePlayer
import com.example.playlistmaker.search.domain.usecase.GetTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerViewModel(
    val getTrackUseCase: GetTrack,
    val prepareTrackUseCase: PrepareTrack,
    private val getPlayerStateUseCase: GetPlayerState,
    private val playTrackUseCase: PlayTrack,
    private val pauseTrackUseCase: PauseTrack,
    private val releasePlayerUseCase: ReleasePlayer,
    private val getFavouriteTracksUseCase: GetFavouriteTracks,
    private val saveToFavouritesUseCase: SaveToFavourites,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatus,
    private val deleteFromFavouritesUseCase: DeleteFromFavourites
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    init {
        prepareTrackUseCase(getTrackUseCase())

        viewModelScope.launch {
            checkFavouriteStatusUseCase(getTrackUseCase().trackId).collect {
                _isFavourite.postValue(it)
            }

            getPlayerStateUseCase().collect { state ->
                _playerState.postValue(state)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun playbackControl() {
        when (_playerState.value) {
            is PlayerState.Playing -> pauseTrackUseCase()
            is PlayerState.Paused, is PlayerState.Prepared, is PlayerState.Completed -> playTrackUseCase()
            else -> Unit
        }
    }

    fun releasePlayer() {
        releasePlayerUseCase()
    }

    fun pausePlayer() {
        if (_playerState.value is PlayerState.Playing)
            pauseTrackUseCase()
    }

    fun saveToFavourites() = viewModelScope.launch(Dispatchers.IO) {
        saveToFavouritesUseCase(getTrackUseCase())
    }

    fun saveOrDeleteFromFavourites() = viewModelScope.launch(Dispatchers.IO) {
        val id = getTrackUseCase().trackId

        checkFavouriteStatusUseCase(id).collect { isFavourite ->
            if (isFavourite) {
                deleteFromFavouritesUseCase(id)
            } else {
                saveToFavouritesUseCase(getTrackUseCase())
            }
        }
    }
}