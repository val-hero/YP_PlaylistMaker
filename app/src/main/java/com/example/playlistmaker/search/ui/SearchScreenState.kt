package com.example.playlistmaker.search.ui

import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.search.domain.model.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object Empty : SearchScreenState()
    data class Content(val tracks: ArrayList<Track>) : SearchScreenState()
    data class History(val tracks: ArrayList<Track>) : SearchScreenState()
    data class Error(val type: ErrorType) : SearchScreenState()
}
