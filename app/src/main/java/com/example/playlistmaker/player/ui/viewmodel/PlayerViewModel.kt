package com.example.playlistmaker.player.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.usecase.GetCurrentPlaybackTime
import com.example.playlistmaker.player.domain.usecase.PauseTrack
import com.example.playlistmaker.player.domain.usecase.PlayTrack
import com.example.playlistmaker.player.domain.usecase.ReleasePlayer
import com.example.playlistmaker.search.domain.usecase.GetTrack
import com.example.playlistmaker.utility.MMSS_FORMAT_PATTERN
import com.example.playlistmaker.utility.TIMER_UPDATE_DELAY

class PlayerViewModel(
    private val mediaPlayerRepository: MediaPlayerRepository,
    getTrackUseCase: GetTrack,
    private val playTrackUseCase: PlayTrack,
    private val pauseTrackUseCase: PauseTrack,
    private val releasePlayerUseCase: ReleasePlayer,
    private val getCurrentPlaybackTimeUseCase: GetCurrentPlaybackTime
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _playbackTime = MutableLiveData<String?>()
    val playbackTime: LiveData<String?> = _playbackTime

    val currentTrack = getTrackUseCase()

    init {
        mediaPlayerRepository.setOnStateChangeListener {
            _playerState.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerRepository.removeOnStateChangeListener()
        handler.removeCallbacksAndMessages(TIMER_UPDATE_TOKEN)
    }

    fun playbackControl() {
        when (_playerState.value) {
            PlayerState.PLAYING -> pauseTrackUseCase()
            PlayerState.PAUSED, PlayerState.PREPARED, PlayerState.COMPLETED -> playTrackUseCase()
            else -> return
        }
    }

    fun runPlaybackTimer() {
        val timerRunnable = object : Runnable {
            override fun run() {
                _playbackTime.postValue(
                    String.format(MMSS_FORMAT_PATTERN, getCurrentPlaybackTimeUseCase()))

                val postTime = SystemClock.uptimeMillis() + TIMER_UPDATE_DELAY
                handler.postAtTime(this, TIMER_UPDATE_TOKEN, postTime)
            }
        }
        handler.post(timerRunnable)
    }

    fun pausePlaybackTimer() {
        handler.removeCallbacksAndMessages(TIMER_UPDATE_TOKEN)
    }

    fun releasePlayer() {
        releasePlayerUseCase()
    }

    fun pausePlayer() {
        pauseTrackUseCase()
    }

    companion object {
        private val TIMER_UPDATE_TOKEN = Any()
    }

}