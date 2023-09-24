package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.usecase.GetSelectedTrack
import com.example.playlistmaker.library.favourite.domain.usecase.CheckFavouriteStatus
import com.example.playlistmaker.library.favourite.domain.usecase.DeleteFromFavourites
import com.example.playlistmaker.library.favourite.domain.usecase.SaveToFavourites
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.usecase.GetPlayerState
import com.example.playlistmaker.player.domain.usecase.PauseTrack
import com.example.playlistmaker.player.domain.usecase.PlayTrack
import com.example.playlistmaker.player.domain.usecase.PrepareTrack
import com.example.playlistmaker.player.domain.usecase.ReleasePlayer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayerViewModel(
    val getSelectedTrackUseCase: GetSelectedTrack,
    val prepareTrackUseCase: PrepareTrack,
    private val getPlayerStateUseCase: GetPlayerState,
    private val playTrackUseCase: PlayTrack,
    private val pauseTrackUseCase: PauseTrack,
    private val releasePlayerUseCase: ReleasePlayer,
    private val saveToFavouritesUseCase: SaveToFavourites,
    private val checkFavouriteStatusUseCase: CheckFavouriteStatus,
    private val deleteFromFavouritesUseCase: DeleteFromFavourites,
) : ViewModel() {
    private val currentTrack = getSelectedTrackUseCase()

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    init {
        prepareTrackUseCase(currentTrack)

        viewModelScope.launch {
            _isFavourite.postValue(checkFavouriteStatusUseCase(currentTrack.id).first())

            getPlayerStateUseCase().collect { state ->
                _playerState.postValue(state)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun togglePlayback() {
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

    fun saveOrDeleteFromFavourites() = viewModelScope.launch {
        checkFavouriteStatusUseCase(currentTrack.id).collect { isFavourite ->
            if (isFavourite) {
                deleteFromFavouritesUseCase(currentTrack.id)
            } else {
                saveToFavouritesUseCase(currentTrack)
            }
            val updatedFavouriteStatus = checkFavouriteStatusUseCase(currentTrack.id).first()
            _isFavourite.postValue(updatedFavouriteStatus)
        }
    }
}
