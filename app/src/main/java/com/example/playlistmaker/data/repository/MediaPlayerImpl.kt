package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.MediaPlayer
import com.example.playlistmaker.domain.repository.TrackRepository

class MediaPlayerImpl(trackRepository: TrackRepository) : MediaPlayer {
    private val player = android.media.MediaPlayer()
    private var stateCallback: ((PlayerState) -> Unit)? = null

    init {
        prepare(trackRepository.getTrack())
    }

    override fun prepare(track: Track) {
        player.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener { stateCallback?.invoke(PlayerState.PREPARED) }
            setOnCompletionListener {  stateCallback?.invoke(PlayerState.COMPLETED) }
        }

    }

    override fun play() {
        player.start()
        stateCallback?.invoke(PlayerState.PLAYING)
    }

    override fun pause() {
        player.pause()
        stateCallback?.invoke(PlayerState.PAUSED)
    }

    override fun release() {
        player.release()
        stateCallback?.invoke(PlayerState.DEFAULT)
    }

    override fun getCurrentPosition(): Long = player.currentPosition.toLong()

    override fun setOnStateChangeListener(callback: (PlayerState) -> Unit) {
        stateCallback = callback
    }

    override fun removeOnStateChangeListener() {
        stateCallback = null
    }

}