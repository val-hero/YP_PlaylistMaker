package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utility.PLAYBACK_UPDATE_DELAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    private var player: MediaPlayer? = null
    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Default)
    private val playerStateFlow: StateFlow<PlayerState> = _playerStateFlow
    private var currentPlaybackTimeJob: Job? = null

    override fun prepare(track: Track) {
        player = MediaPlayer()
        track.previewUrl?.let {
            player?.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnPreparedListener { _playerStateFlow.value = PlayerState.Prepared(track) }
                setOnCompletionListener {
                    _playerStateFlow.value = PlayerState.Completed
                    currentPlaybackTimeJob?.cancel()
                }
            }
        } ?: _playerStateFlow.update { PlayerState.Error("This track can't be played") }

    }

    override fun play() {
        player?.start()
        currentPlaybackTimeJob?.cancel()
        currentPlaybackTimeJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                _playerStateFlow.value = PlayerState.Playing(getCurrentPosition())
                delay(PLAYBACK_UPDATE_DELAY)
            }
        }
    }

    override fun pause() {
        player?.pause()
        _playerStateFlow.value = PlayerState.Paused
        currentPlaybackTimeJob?.cancel()

    }

    override fun release() {
        player?.release()
        _playerStateFlow.value = PlayerState.Default
        currentPlaybackTimeJob?.cancel()
        player = null

    }

    override fun getCurrentState(): StateFlow<PlayerState> {
        return playerStateFlow
    }

    private fun getCurrentPosition(): Long? = player?.currentPosition?.toLong()
}