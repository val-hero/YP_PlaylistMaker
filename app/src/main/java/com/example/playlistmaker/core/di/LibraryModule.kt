package com.example.playlistmaker.core.di

import com.example.playlistmaker.library.ui.viewmodel.FavouriteTracksFragmentViewModel
import com.example.playlistmaker.library.ui.viewmodel.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {

    viewModel<FavouriteTracksFragmentViewModel> {
        FavouriteTracksFragmentViewModel()
    }

    viewModel<PlaylistFragmentViewModel> {
        PlaylistFragmentViewModel()
    }
}