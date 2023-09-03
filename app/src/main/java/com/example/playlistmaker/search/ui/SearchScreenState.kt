package com.example.playlistmaker.search.ui

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utility.ErrorType

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object Empty : SearchScreenState()
    data class Content(val tracks: ArrayList<Track>) : SearchScreenState()
    data class History(val tracks: ArrayList<Track>) : SearchScreenState()
    data class Error(val type: ErrorType) : SearchScreenState()
}
