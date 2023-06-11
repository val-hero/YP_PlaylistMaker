package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.model.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    object EmptyInput: SearchScreenState()
    data class HasInput(val input: String): SearchScreenState()
    data class Content(val tracks: ArrayList<Track>) : SearchScreenState()
    data class History(val tracks: ArrayList<Track>) : SearchScreenState()
    data class Error(val code: Int) : SearchScreenState()
}
