package com.example.playlistmaker.player.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.usecase.GetTrack
import com.example.playlistmaker.utility.MMSS_FORMAT_PATTERN
import com.example.playlistmaker.utility.TIMER_UPDATE_DELAY
import kotlinx.coroutines.launch

class PlayerViewModel(
    getTrackUseCase: GetTrack,
    prepareTrackUseCase: PrepareTrack,
    private val getPlayerStateUseCase: GetPlayerStateUseCase,
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
        prepareTrackUseCase(currentTrack)
        viewModelScope.launch {
            getPlayerStateUseCase().collect { state ->
                _playerState.postValue(state)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(TIMER_UPDATE_TOKEN)
        releasePlayer()
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
        if(_playerState.value == PlayerState.PLAYING)
            pauseTrackUseCase()
    }

    companion object {
        private val TIMER_UPDATE_TOKEN = Any()
    }

}