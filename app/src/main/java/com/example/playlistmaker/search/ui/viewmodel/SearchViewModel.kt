package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetTrackList
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
import com.example.playlistmaker.search.domain.usecase.SaveTrack
import com.example.playlistmaker.search.domain.usecase.SaveTrackList
import com.example.playlistmaker.search.domain.usecase.Search
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.utility.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val saveToHistoryUseCase: SaveToHistory,
    private val clearSearchHistoryUseCase: ClearSearchHistory
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val searchHistory = ArrayList<Track>()
    private var searchInput: String? = null

    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private val _trackIsClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = _trackIsClickable

    private var latestSearchExpression: CharSequence = ""

    private var searchFieldIsFocused = false

    private var searchJob: Job? = null

    init {
        loadHistory()
    }

    private fun search(expression: CharSequence) {
        _screenState.value = SearchScreenState.Loading
        latestSearchExpression = expression

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchUseCase(expression).collect { result ->
                when (result) {
                    is Result.Error -> _screenState.value =
                        SearchScreenState.Error(result.errorType)

                    is Result.Success -> _screenState.value =
                        SearchScreenState.Content(result.data as ArrayList)
                }
            }
        }
    }

    fun runLatestSearch() {
        search(latestSearchExpression)
    }

    fun onSearchTextChanged(newText: String?) {
        searchInput = newText
        toggleHistoryVisibility()
        if (!newText.isNullOrBlank())
            search(newText)
        else
            searchJob?.cancel()
    }

    fun onSearchFieldFocusChanged(isFocused: Boolean) {
        searchFieldIsFocused = isFocused
        toggleHistoryVisibility()
    }

    private fun toggleHistoryVisibility() {
        if (searchFieldIsFocused && searchInput.isNullOrBlank())
            _screenState.value = SearchScreenState.History(searchHistory)
        else
            _screenState.value = SearchScreenState.Empty
    }

    fun trackClickDebounce() {
        _trackIsClickable.value = false
        viewModelScope.launch {
            delay(TRACK_CLICK_DELAY)
            _trackIsClickable.value = true
        }
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

    private fun loadHistory() {
        searchHistory.clear()
        searchHistory.addAll(getTrackListUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        saveTrackListUseCase(searchHistory)
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        val SEARCH_REQUEST_TOKEN = Any()
        const val SEARCH_DEBOUNCE_DELAY = 1000L
        const val TRACK_CLICK_DELAY = 100L
    }
}