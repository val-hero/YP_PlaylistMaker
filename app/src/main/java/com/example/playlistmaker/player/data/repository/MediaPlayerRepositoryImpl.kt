package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerRepositoryImpl : MediaPlayerRepository {
    private var player: MediaPlayer? = null
    private val _playerStateFlow = MutableStateFlow(PlayerState.DEFAULT)
    private val playerStateFlow: StateFlow<PlayerState> = _playerStateFlow

    override fun prepare(track: Track) {
        player = MediaPlayer()
        player?.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener { _playerStateFlow.value = PlayerState.PREPARED }
            setOnCompletionListener { _playerStateFlow.value = PlayerState.COMPLETED }
        }
    }

    override fun play() {
        player?.start()
        _playerStateFlow.value = PlayerState.PLAYING
    }

    override fun pause() {
        player?.pause()
        _playerStateFlow.value = PlayerState.PAUSED
    }

    override fun release() {
        player?.release()
        _playerStateFlow.value = PlayerState.DEFAULT
        player = null
    }

    override fun getCurrentPosition(): Long? = player?.currentPosition?.toLong()

    override fun getCurrentState(): StateFlow<PlayerState> {
        return playerStateFlow
    }
}