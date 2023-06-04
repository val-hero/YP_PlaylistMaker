package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import kotlinx.coroutines.flow.StateFlow

class GetPlayerStateUseCase(private val mediaPlayerRepository: MediaPlayerRepository) {

    operator fun invoke(): StateFlow<PlayerState> {
        return mediaPlayerRepository.getCurrentState()
    }
}