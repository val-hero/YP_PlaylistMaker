package com.example.playlistmaker.core.di

import com.example.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.usecase.GetPlayerState
import com.example.playlistmaker.player.domain.usecase.PauseTrack
import com.example.playlistmaker.player.domain.usecase.PlayTrack
import com.example.playlistmaker.player.domain.usecase.PrepareTrack
import com.example.playlistmaker.player.domain.usecase.ReleasePlayer
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    viewModel {
        PlayerViewModel(
            getSelectedTrackUseCase = get(),
            prepareTrackUseCase = get(),
            playTrackUseCase = get(),
            pauseTrackUseCase = get(),
            getPlayerStateUseCase = get(),
            releasePlayerUseCase = get(),
            saveToFavouritesUseCase = get(),
            checkFavouriteStatusUseCase = get(),
            deleteFromFavouritesUseCase = get()
        )
    }

    single<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl()
    }

    factory {
        PrepareTrack(mediaPlayerRepository = get())
    }

    factory {
        PlayTrack(mediaPlayerRepository = get())
    }

    factory {
        PauseTrack(mediaPlayerRepository = get())
    }

    factory {
        ReleasePlayer(mediaPlayerRepository = get())
    }

    factory {
        GetPlayerState(mediaPlayerRepository = get())
    }
}