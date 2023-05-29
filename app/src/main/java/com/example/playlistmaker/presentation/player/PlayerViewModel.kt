package com.example.playlistmaker.presentation.player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.domain.usecase.GetTrack
import com.example.playlistmaker.domain.usecase.player.PauseTrack
import com.example.playlistmaker.domain.usecase.player.PlayTrack
import com.example.playlistmaker.domain.usecase.player.ReleasePlayer
import com.example.playlistmaker.utility.MMSS_FORMAT_PATTERN

class PlayerViewModel(
    private val mediaPlayerRepository: MediaPlayerRepository,
    getTrackUseCase: GetTrack,
    private val playTrackUseCase: PlayTrack,
    private val pauseTrackUseCase: PauseTrack,
    private val releasePlayerUseCase: ReleasePlayer
) : ViewModel() {
    val playerState = MutableLiveData<PlayerState>()
    val playbackTime = MutableLiveData<String>()
    val currentTrack = getTrackUseCase()

    init {
        mediaPlayerRepository.setOnStateChangeListener {
            playerState.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerRepository.removeOnStateChangeListener()
    }

    fun playbackControl() {
        when (playerState.value) {
            PlayerState.PLAYING -> pauseTrackUseCase()
            PlayerState.PAUSED, PlayerState.PREPARED, PlayerState.COMPLETED -> playTrackUseCase()
            else -> return
        }
    }

    fun updatePlaybackTime() {
        playbackTime.value =
            String.format(MMSS_FORMAT_PATTERN, mediaPlayerRepository.getCurrentPosition())
    }

    fun releasePlayer() {
        releasePlayerUseCase()
    }

    fun pausePlayer() {
        pauseTrackUseCase()
    }
}