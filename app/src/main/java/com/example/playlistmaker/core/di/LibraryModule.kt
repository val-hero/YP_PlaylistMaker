package com.example.playlistmaker.core.di

import com.example.playlistmaker.library.favourite.data.repository.FavouriteTracksRepositoryImpl
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import com.example.playlistmaker.library.favourite.domain.usecase.CheckFavouriteStatus
import com.example.playlistmaker.library.favourite.domain.usecase.DeleteFromFavourites
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import com.example.playlistmaker.library.favourite.domain.usecase.SaveToFavourites
import com.example.playlistmaker.library.favourite.ui.FavouriteTracksFragmentViewModel
import com.example.playlistmaker.library.ui.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel<FavouriteTracksFragmentViewModel> {
        FavouriteTracksFragmentViewModel()
    }

    viewModel<PlaylistFragmentViewModel> {
        PlaylistFragmentViewModel()
    }

    single<FavouriteTracksRepository> {
        FavouriteTracksRepositoryImpl(appDatabase = get())
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