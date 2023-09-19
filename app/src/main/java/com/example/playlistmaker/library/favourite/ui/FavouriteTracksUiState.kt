package com.example.playlistmaker.library.favourite.ui

import com.example.playlistmaker.core.domain.model.Track

sealed class FavouriteTracksUiState {

    data class Content(val data: List<Track>) : FavouriteTracksUiState()

    object Empty : FavouriteTracksUiState()
}