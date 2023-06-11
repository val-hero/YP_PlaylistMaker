package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository

class GetCurrentPlaybackTime(private val mediaPlayerRepository: MediaPlayerRepository) {

    operator fun invoke(): Long? = mediaPlayerRepository.getCurrentPosition()
}