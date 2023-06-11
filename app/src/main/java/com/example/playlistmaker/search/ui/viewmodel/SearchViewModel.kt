package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetTrackList
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
import com.example.playlistmaker.search.domain.usecase.SaveTrack
import com.example.playlistmaker.search.domain.usecase.SaveTrackList
import com.example.playlistmaker.search.domain.usecase.Search
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.utility.Result
import com.example.playlistmaker.utility.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker.utility.TRACK_CLICK_DELAY

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

    init {
        loadHistory()
    }

    private fun searchDebounce(expression: CharSequence) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { search(expression) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
    }

    private fun search(expression: CharSequence) {
        _screenState.value = SearchScreenState.Loading
        latestSearchExpression = expression

        searchUseCase(
            expression = expression,
            callback = { result ->
                when (result) {
                    is Result.Success -> {
                        _screenState.value = SearchScreenState.Content(result.data)
                    }
                    is Result.Error -> {
                        _screenState.value = SearchScreenState.Error(result.resultCode)
                    }
                }
            })
    }

    fun runLatestSearch() {
        searchDebounce(latestSearchExpression)
    }

    fun onSearchTextChanged(newText: String?) {
        searchInput = newText
        toggleHistoryVisibility()
        if (!newText.isNullOrBlank())
            searchDebounce(newText)
        else
            handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
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
        handler.postDelayed({ _trackIsClickable.value = true }, TRACK_CLICK_DELAY)
    }

    fun clearSearchField() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        searchInput = ""
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
    }
}