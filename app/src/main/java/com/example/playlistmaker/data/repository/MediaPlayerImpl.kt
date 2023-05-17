package com.example.playlistmaker.data.repository

import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.MediaPlayer
import com.example.playlistmaker.domain.repository.TrackRepository

class MediaPlayerImpl(trackRepository: TrackRepository) : MediaPlayer {
    private val player = android.media.MediaPlayer()
    private var playerState: PlayerState = PlayerState.DEFAULT

    init {
        prepare(trackRepository.getTrack())
    }

    override fun prepare(track: Track) {
        player.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener { playerState = PlayerState.PREPARED }
            setOnCompletionListener { playerState = PlayerState.COMPLETED }
        }

    }

    override fun play() {
        player.start()
        playerState = PlayerState.PLAYING
    }

    override fun pause() {
        player.pause()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        player.release()
        playerState = PlayerState.DEFAULT
    }

    override fun getCurrentState(): PlayerState = playerState

    override fun getCurrentPosition(): Long = player.currentPosition.toLong()

}