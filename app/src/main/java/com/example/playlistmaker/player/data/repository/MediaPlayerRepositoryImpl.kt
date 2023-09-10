package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.core.utils.loopWithDelay
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    private var player: MediaPlayer? = null
    private val _playerStateFlow = MutableStateFlow<PlayerState>(PlayerState.Default)
    private val playerStateFlow: StateFlow<PlayerState> = _playerStateFlow

    private val updatePlaybackTime = loopWithDelay(
        delayMillis = PLAYBACK_UPDATE_DELAY,
        coroutineScope = CoroutineScope(Dispatchers.Default),
        loopCondition = {
            player?.isPlaying == true
        },
        action = {
            _playerStateFlow.value = PlayerState.Playing(getCurrentPosition())
        }
    )

    override fun prepare(track: Track) {
        player = MediaPlayer()

        if (track.previewUrl.isBlank()) {
            _playerStateFlow.value = PlayerState.Error(ErrorType.ACTION_CANT_BE_PERFORMED)
        } else {
            player?.apply {
                setDataSource(track.previewUrl)
                prepareAsync()
                setOnErrorListener { _, _, _ ->
                    _playerStateFlow.value = PlayerState.Error(ErrorType.FAILED_TO_LOAD)
                    true
                }
                setOnPreparedListener { _playerStateFlow.value = PlayerState.Prepared(track) }
                setOnCompletionListener { _playerStateFlow.value = PlayerState.Completed }
            }
        }
    }

    override fun play() {
        player?.start()
        updatePlaybackTime()
    }

    override fun pause() {
        player?.pause()
        _playerStateFlow.value = PlayerState.Paused
    }

    override fun release() {
        player?.release()
        _playerStateFlow.value = PlayerState.Default
        player = null
    }

    override fun getCurrentState(): StateFlow<PlayerState> {
        return playerStateFlow
    }

    private fun getCurrentPosition(): Long? = player?.currentPosition?.toLong()

    companion object {
        const val PLAYBACK_UPDATE_DELAY = 300L
    }
}