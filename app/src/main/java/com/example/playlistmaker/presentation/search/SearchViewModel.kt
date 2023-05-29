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
import com.example.playlistmaker.domain.usecase.search.SaveToHistory
import com.example.playlistmaker.domain.usecase.search.Search
import com.example.playlistmaker.utility.Result

class SearchViewModel(
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val saveToHistoryUseCase: SaveToHistory,
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
        getHistory()
    }

    fun search() {
        searchUseCase(
            searchText = _searchInput.value ?: "",
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
    }
}