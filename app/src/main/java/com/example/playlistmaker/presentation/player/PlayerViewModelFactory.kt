package com.example.playlistmaker.presentation.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.domain.usecase.GetTrack
import com.example.playlistmaker.domain.usecase.player.PauseTrack
import com.example.playlistmaker.domain.usecase.player.PlayTrack
import com.example.playlistmaker.domain.usecase.player.ReleasePlayer

class PlayerViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val trackStorage by lazy { SharedPrefsTrackStorage(context) }
    private val trackRepository by lazy { TrackRepositoryImpl(trackStorage) }
    private val mediaPlayerRepository by lazy { MediaPlayerRepositoryImpl(trackRepository) }
    private val getTrackUseCase by lazy { GetTrack(trackRepository) }
    private val playTrackUseCase by lazy { PlayTrack(mediaPlayerRepository) }
    private val pauseTrackUseCase by lazy { PauseTrack(mediaPlayerRepository) }
    private val releasePlayerUseCase by lazy { ReleasePlayer(mediaPlayerRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            mediaPlayerRepository,
            getTrackUseCase,
            playTrackUseCase,
            pauseTrackUseCase,
            releasePlayerUseCase
        ) as T
    }
}