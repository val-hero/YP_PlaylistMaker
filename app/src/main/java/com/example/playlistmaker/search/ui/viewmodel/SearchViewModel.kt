package com.example.playlistmaker.search.ui.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.usecase.*
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.utility.Result
import com.example.playlistmaker.utility.SEARCH_DEBOUNCE_DELAY

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val saveToHistoryUseCase: SaveToHistory,
    private val clearSearchHistoryUseCase: ClearSearchHistory
): ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val _searchHistory = MutableLiveData<ArrayList<Track>>()
    private val _screenState = MutableLiveData<SearchScreenState>()
    val screenState: LiveData<SearchScreenState> = _screenState

    private var latestSearchExpression: CharSequence = ""

    init {
        getHistory()
    }

    fun searchDebounce(expression: CharSequence) {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)

        val searchRunnable = Runnable { search(expression) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(searchRunnable, SEARCH_DEBOUNCE_TOKEN, postTime)
    }

    private fun search(expression: CharSequence) {
        _screenState.value = SearchScreenState.Loading
        latestSearchExpression = expression

        searchUseCase(
            expression = expression,
            callback = { result ->
                when(result) {
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

    fun setupTextWatcher(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if(!text.isNullOrBlank())
                    _screenState.value = SearchScreenState.HasInput(text.toString())
                else {
                    _screenState.value = SearchScreenState.EmptyInput
                    handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
                }
        }

            override fun afterTextChanged(text: Editable?) {

            }
        }
    }

    fun onSearchFieldFocused() {
        if(latestSearchExpression.isBlank())
            _screenState.value = SearchScreenState.History(_searchHistory.value ?: arrayListOf())
    }

    fun clearSearchField() {
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
        _screenState.value = SearchScreenState.EmptyInput
    }

    fun clearSearchHistory() {
        clearSearchHistoryUseCase()
        _searchHistory.value = arrayListOf()
        _screenState.value = SearchScreenState.EmptyInput
    }

    fun saveToHistory(track: Track) {
        saveToHistoryUseCase(track, _searchHistory.value ?: arrayListOf())
        getHistory()
    }

    fun saveTrack(track: Track) {
        saveTrackUseCase(track)
    }

    private fun getHistory() {
        _searchHistory.value = getTrackListUseCase()
    }

    override fun onCleared() {
        super.onCleared()
        saveTrackListUseCase(_searchHistory.value ?: arrayListOf())
        handler.removeCallbacksAndMessages(SEARCH_DEBOUNCE_TOKEN)
    }

    companion object {
        val SEARCH_DEBOUNCE_TOKEN = Any()
    }
}