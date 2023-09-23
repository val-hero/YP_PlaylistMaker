package com.example.playlistmaker.core.di

import com.example.playlistmaker.library.create_playlist.domain.usecase.SavePlaylist
import com.example.playlistmaker.library.create_playlist.ui.CreatePlaylistViewModel
import com.example.playlistmaker.library.favourite.data.repository.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import com.example.playlistmaker.library.favourite.domain.usecase.CheckFavouriteStatus
import com.example.playlistmaker.library.favourite.domain.usecase.DeleteFromFavourites
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import com.example.playlistmaker.library.favourite.domain.usecase.SaveToFavourites
import com.example.playlistmaker.library.favourite.ui.FavouriteTracksFragmentViewModel
import com.example.playlistmaker.library.playlists.domain.repository.PlaylistRepository
import com.example.playlistmaker.library.playlists.domain.usecase.GetPlaylists
import com.example.playlistmaker.library.playlists.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.library.playlists.ui.PlaylistsFragmentViewModel
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
        CreatePlaylistViewModel(savePlaylistUseCase = get())
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