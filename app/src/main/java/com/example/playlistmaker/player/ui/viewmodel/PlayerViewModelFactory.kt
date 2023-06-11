package com.example.playlistmaker.player.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.usecase.GetCurrentPlaybackTime
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.search.domain.usecase.GetTrack
import com.example.playlistmaker.player.domain.usecase.PauseTrack
import com.example.playlistmaker.player.domain.usecase.PlayTrack
import com.example.playlistmaker.player.domain.usecase.ReleasePlayer

class PlayerViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val trackStorage by lazy(LazyThreadSafetyMode.NONE) { SharedPrefsTrackStorage(context) }
    private val trackRepository by lazy(LazyThreadSafetyMode.NONE) { TrackRepositoryImpl(trackStorage) }
    private val mediaPlayerRepository by lazy(LazyThreadSafetyMode.NONE) { MediaPlayerRepositoryImpl(trackRepository) }
    private val getTrackUseCase by lazy(LazyThreadSafetyMode.NONE) { GetTrack(trackRepository) }
    private val playTrackUseCase by lazy(LazyThreadSafetyMode.NONE) { PlayTrack(mediaPlayerRepository) }
    private val pauseTrackUseCase by lazy(LazyThreadSafetyMode.NONE) { PauseTrack(mediaPlayerRepository) }
    private val releasePlayerUseCase by lazy(LazyThreadSafetyMode.NONE) { ReleasePlayer(mediaPlayerRepository) }
    private val getCurrentPlaybackTime by lazy(LazyThreadSafetyMode.NONE) { GetCurrentPlaybackTime(mediaPlayerRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlayerViewModel(
            mediaPlayerRepository = mediaPlayerRepository,
            getTrackUseCase = getTrackUseCase,
            playTrackUseCase = playTrackUseCase,
            pauseTrackUseCase = pauseTrackUseCase,
            releasePlayerUseCase = releasePlayerUseCase,
            getCurrentPlaybackTimeUseCase = getCurrentPlaybackTime
        ) as T
    }
}