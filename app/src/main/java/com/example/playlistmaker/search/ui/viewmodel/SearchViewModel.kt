package com.example.playlistmaker.search.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.core.utils.debounce
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetTrackList
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
import com.example.playlistmaker.search.domain.usecase.SaveTrack
import com.example.playlistmaker.search.domain.usecase.SaveTrackList
import com.example.playlistmaker.search.domain.usecase.Search
import com.example.playlistmaker.search.ui.SearchScreenState
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val saveToHistoryUseCase: SaveToHistory,
    private val clearSearchHistoryUseCase: ClearSearchHistory
) : ViewModel() {

    private val searchHistory = ArrayList<Track>()

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    var trackIsClickable = true
        private set

    private var latestSearchExpression: CharSequence? = null
    private var latestSearchResult: ArrayList<Track> = arrayListOf()

    private var searchFieldIsFocused = false

    private val trackSearchDebounce =
        debounce<CharSequence>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { expression ->
            search(expression)
        }

    private val trackClickDebounce =
        debounce<Boolean>(CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            trackIsClickable = it
        }

    init {
        loadHistory()
    }

    private fun search(expression: CharSequence) {
        latestSearchExpression = expression
        if (expression.isBlank()) return

        _screenState.value = SearchScreenState.Loading

        viewModelScope.launch {
            searchUseCase(expression).collect { result ->
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
        trackSearchDebounce(latestSearchExpression ?: "")
    }

    fun onSearchExpressionChange(newExpression: String?) {
        toggleHistoryVisibility()

        if (latestSearchExpression == newExpression && latestSearchResult.isNotEmpty())
            _screenState.value = SearchScreenState.Content(latestSearchResult)
        else
            trackSearchDebounce(newExpression ?: "")
    }

    fun onSearchFieldFocusChanged(isFocused: Boolean) {
        searchFieldIsFocused = isFocused
        toggleHistoryVisibility()
    }

    private fun toggleHistoryVisibility() {
        if (searchFieldIsFocused && latestSearchExpression.isNullOrBlank())
            _screenState.value = SearchScreenState.History(searchHistory)
        else
            _screenState.value = SearchScreenState.Empty
    }

    fun clearSearchHistory() {
        clearSearchHistoryUseCase()
        searchHistory.clear()
        _screenState.value = SearchScreenState.Empty
    }

    fun saveToHistory(track: Track) {
        saveToHistoryUseCase(track, searchHistory)
        loadHistory()
    }

    fun saveTrack(track: Track) {
        saveTrackUseCase(track)
    }

    fun loadHistory() {
        searchHistory.clear()
        searchHistory.addAll(getTrackListUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        saveTrackListUseCase(searchHistory)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 300L
    }
}