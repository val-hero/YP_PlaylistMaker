package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.repository.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.player.ui.viewmodel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    viewModel {
        PlayerViewModel(
            getTrackUseCase = get(),
            prepareTrackUseCase = get(),
            playTrackUseCase = get(),
            pauseTrackUseCase = get(),
            getPlayerStateUseCase = get(),
            releasePlayerUseCase = get()
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