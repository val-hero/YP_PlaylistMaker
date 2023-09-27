package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.core.utils.debounce
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetSearchHistory
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
import com.example.playlistmaker.search.domain.usecase.Search
import com.example.playlistmaker.search.ui.SearchScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackUseCase: SaveTrack,
    private val saveToHistoryUseCase: SaveToHistory,
    private val getSearchHistoryUseCase: GetSearchHistory,
    private val clearSearchHistoryUseCase: ClearSearchHistory
) : ViewModel() {
    private var searchHistory = arrayListOf<Track>()

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    var trackIsClickable = true
        private set

    private var latestSearchQuery: String? = null
    private var latestSearchResult: ArrayList<Track> = arrayListOf()

    private var searchFieldIsFocused = false

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
            search(query)
        }

    private val trackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            trackIsClickable = it
        }

    private fun search(query: String) {
        if (query.isBlank()) return

        latestSearchQuery = query
        _screenState.value = SearchScreenState.Loading
        viewModelScope.launch {
            searchUseCase(query).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _screenState.value = SearchScreenState.Content(result.data as ArrayList)
                        latestSearchResult = result.data
                    }

                    is Result.Error -> {
                        _screenState.value = SearchScreenState.Error(result.errorType)
                    }
                }
            }
        }
    }

    fun onTrackClick() {
        trackIsClickable = false
        trackClickDebounce(true)
    }

    fun runLatestSearch() {
        trackSearchDebounce(latestSearchQuery ?: "")
    }

    fun onSearchQueryChanged(newQuery: String?) {
        trackSearchDebounce(newQuery ?: "")

        if (newQuery.isNullOrEmpty())
            latestSearchQuery = null

        if (newQuery.isNullOrEmpty() && searchFieldIsFocused) {
            fetchSearchHistory()
        } else {
            _screenState.value = SearchScreenState.Empty
        }
    }

    fun getLatestSearchResult() {
        if (!latestSearchQuery.isNullOrBlank() && latestSearchResult.isNotEmpty())
            _screenState.value = SearchScreenState.Content(latestSearchResult)
    }

    fun onSearchFieldFocusChanged(isFocused: Boolean) {
        searchFieldIsFocused = isFocused

        if (isFocused && latestSearchQuery.isNullOrEmpty()) {
            fetchSearchHistory()
        } else if (!isFocused && latestSearchQuery.isNullOrEmpty()) {
            _screenState.value = SearchScreenState.Empty
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            clearSearchHistoryUseCase()
        }
        _screenState.value = SearchScreenState.Empty
    }

    fun saveToHistory(track: Track) = viewModelScope.launch(Dispatchers.IO) {
        saveToHistoryUseCase(track)
    }

    fun saveTrack(track: Track) {
        saveTrackUseCase(track)
    }

    private fun fetchSearchHistory() {
        viewModelScope.launch {
            val history = getSearchHistoryUseCase().firstOrNull() ?: emptyList()
            searchHistory.clear()
            searchHistory.addAll(history)
            _screenState.value = SearchScreenState.History(ArrayList(searchHistory))
        }
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}