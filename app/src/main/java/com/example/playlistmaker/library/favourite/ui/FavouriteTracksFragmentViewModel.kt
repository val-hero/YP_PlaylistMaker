package com.example.playlistmaker.library.favourite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import kotlinx.coroutines.launch

class FavouriteTracksFragmentViewModel(
    private val getFavouriteTracksUseCase: GetFavouriteTracks
) : ViewModel() {

    private val _favouriteTracks = MutableLiveData<List<Track>>()
    val favouriteTracks: LiveData<List<Track>> = _favouriteTracks

    private val _uiState = MutableLiveData<FavouriteTracksUiState>()
    val uiState: LiveData<FavouriteTracksUiState> = _uiState

    fun getFavouriteTracks() = viewModelScope.launch {
        getFavouriteTracksUseCase().collect {
            _favouriteTracks.postValue(it)
        }
    }
}