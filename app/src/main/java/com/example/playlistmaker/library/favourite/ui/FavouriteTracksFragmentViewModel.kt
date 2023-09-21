package com.example.playlistmaker.library.favourite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.library.favourite.domain.usecase.GetFavouriteTracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteTracksFragmentViewModel(
    private val saveTrackUseCase: SaveTrack,
    private val getFavouriteTracksUseCase: GetFavouriteTracks
) : ViewModel() {

    private val _uiState = MutableLiveData<FavouriteTracksUiState>()
    val uiState: LiveData<FavouriteTracksUiState> = _uiState

    fun getFavouriteTracks() = viewModelScope.launch(Dispatchers.IO) {
        getFavouriteTracksUseCase().collect {
            if(it.isEmpty())
                _uiState.postValue(FavouriteTracksUiState.Empty)
            else
                _uiState.postValue(FavouriteTracksUiState.Content(it))
        }
    }

    fun saveSelectedTrack(track: Track) {
        saveTrackUseCase(track)
    }
}