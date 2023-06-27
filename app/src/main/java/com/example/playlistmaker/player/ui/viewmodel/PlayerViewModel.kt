package com.example.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.usecase.GetTrack
import kotlinx.coroutines.launch

class PlayerViewModel(
    val getTrackUseCase: GetTrack,
    val prepareTrackUseCase: PrepareTrack,
    private val getPlayerStateUseCase: GetPlayerState,
    private val playTrackUseCase: PlayTrack,
    private val pauseTrackUseCase: PauseTrack,
    private val releasePlayerUseCase: ReleasePlayer,
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    init {
        prepareTrackUseCase(getTrackUseCase())

        viewModelScope.launch {
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
}