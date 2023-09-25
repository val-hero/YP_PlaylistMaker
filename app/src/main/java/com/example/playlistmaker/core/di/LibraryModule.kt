package com.example.playlistmaker.core.di

import com.example.playlistmaker.library.favourite.data.repository.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import com.example.playlistmaker.library.favourite.domain.usecase.CheckFavouriteStatus
import com.example.playlistmaker.library.favourite.domain.usecase.DeleteFromFavourites
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import com.example.playlistmaker.library.favourite.domain.usecase.SaveToFavourites
import com.example.playlistmaker.library.favourite.ui.FavouriteTracksFragmentViewModel
import com.example.playlistmaker.library.playlist_creation.ui.CreatePlaylistViewModel
import com.example.playlistmaker.library.playlist_details.ui.PlaylistDetailsViewModel
import com.example.playlistmaker.library.playlists.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository
import com.example.playlistmaker.library.playlists.domain.usecase.DeletePlaylist
import com.example.playlistmaker.library.playlists.domain.usecase.DeleteTrack
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylistDetails
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylists
import com.example.playlistmaker.library.playlists.domain.usecase.GetTracksInPlaylist
import com.example.playlistmaker.library.playlists.domain.usecase.SavePlaylist
import com.example.playlistmaker.library.playlists.domain.usecase.UpdatePlaylist
import com.example.playlistmaker.library.playlists.ui.PlaylistsFragmentViewModel
import com.example.playlistmaker.player.domain.usecase.SaveToPlaylist
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel<FavouriteTracksFragmentViewModel> {
        FavouriteTracksFragmentViewModel(
            getFavouriteTracksUseCase = get(),
            saveTrackUseCase = get()
        )
    }

    viewModel<PlaylistsFragmentViewModel> {
        PlaylistsFragmentViewModel(getPlaylistsUseCase = get())
    }

    viewModel<CreatePlaylistViewModel>() {
        CreatePlaylistViewModel(
            savePlaylistUseCase = get(),
            getPlaylistDetailsUseCase = get(),
            updatePlaylistUseCase = get()
        )
    }

    viewModel {
        PlaylistDetailsViewModel(
            getPlaylistDetailsUseCase = get(),
            getTracksInPlaylistUseCase = get(),
            saveTrackUseCase = get(),
            deleteTrackUseCase = get(),
            deletePlaylistUseCase = get()
        )
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            database = get(),
            context = androidContext()
        )
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get())
    }

    factory {
        UpdatePlaylist(playlistRepository = get())
    }

    factory {
        DeleteTrack(playlistRepository = get())
    }

    factory {
        DeletePlaylist(playlistRepository = get())
    }

    factory {
        GetPlaylistDetails(playlistRepository = get())
    }

    factory {
        GetTracksInPlaylist(playlistRepository = get())
    }

    factory {
        SaveToPlaylist(playlistRepository = get())
    }

    factory {
        SavePlaylist(playlistRepository = get())
    }

    factory {
        GetPlaylists(playlistRepository = get())
    }

    factory<GetFavouriteTracks> {
        GetFavouriteTracks(favTracksRepository = get())
    }

    factory<SaveToFavourites> {
        SaveToFavourites(favTracksRepository = get())
    }

    factory<CheckFavouriteStatus> {
        CheckFavouriteStatus(favTracksRepository = get())
    }

    factory {
        DeleteFromFavourites(favTracksRepository = get())
    }
}