package com.example.playlistmaker.presentation.search

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecase.GetTrackList
import com.example.playlistmaker.domain.usecase.SaveTrack
import com.example.playlistmaker.domain.usecase.SaveTrackList
import com.example.playlistmaker.domain.usecase.search.ClearSearchHistory
import com.example.playlistmaker.domain.usecase.search.Search
import com.example.playlistmaker.utility.HISTORY_CAPACITY
import com.example.playlistmaker.utility.Result

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val clearSearchHistoryUseCase: ClearSearchHistory
): ViewModel() {
    private val _searchInput = MutableLiveData<CharSequence>()
    val searchInput: LiveData<CharSequence> = _searchInput
    private val _searchResult = MutableLiveData<Result<ArrayList<Track>>?>()
    val searchResult: LiveData<Result<ArrayList<Track>>?> = _searchResult
    private val _progressBarVisibility = MutableLiveData<Boolean>()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility
    private val _searchHistory = MutableLiveData<ArrayList<Track>>()
    val searchHistory: LiveData<ArrayList<Track>> = _searchHistory

    init {
        loadHistory()
    }

    fun search() {
        searchUseCase(
            searchText = _searchInput.value!!,
            callback = { result ->
                _searchResult.value = result
                _progressBarVisibility.value = false
            })
    }

    fun setupTextWatcher(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                _searchInput.value = text ?: ""
                _progressBarVisibility.value = !text.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.isNullOrBlank())
                    _searchResult.value = null
            }
        }
    }

    fun clearSearchHistory() {
        clearSearchHistoryUseCase()
        _searchHistory.value = arrayListOf()
    }

    fun saveToHistory(track: Track) {
        fun removeDuplicate(tracks: ArrayList<Track>) {
            val iterator = tracks.iterator()
            while (iterator.hasNext()) {
                val currentTrack = iterator.next()
                if (track.trackId == currentTrack.trackId) {
                    iterator.remove()
                }
            }
        }

        val history = _searchHistory.value ?: arrayListOf()
        removeDuplicate(history)
        if (history.size >= HISTORY_CAPACITY) {
            history.removeLast()
        }
        history.add(0, track)
        _searchHistory.value = history
        saveTrackListUseCase(history)
    }

    fun saveTrack(track: Track) {
        saveTrackUseCase(track)
    }

    private fun loadHistory() {
        _searchHistory.value = getTrackListUseCase()
    }

    override fun onCleared() {
        super.onCleared()
        saveTrackListUseCase(_searchHistory.value ?: arrayListOf())
    }
}