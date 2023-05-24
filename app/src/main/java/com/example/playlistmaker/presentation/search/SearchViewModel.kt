package com.example.playlistmaker.presentation.search

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.domain.usecase.GetTrackList
import com.example.playlistmaker.domain.usecase.SaveTrack
import com.example.playlistmaker.domain.usecase.SaveTrackList
import com.example.playlistmaker.domain.usecase.search.ClearSearchHistory
import com.example.playlistmaker.domain.usecase.search.Search
import com.example.playlistmaker.utility.Result

class SearchViewModel(
    private val trackRepository: TrackRepository,
    private val searchUseCase: Search,
    private val saveTrackListUseCase: SaveTrackList,
    private val getTrackListUseCase: GetTrackList,
    private val saveTrackUseCase: SaveTrack,
    private val clearSearchHistoryUseCase: ClearSearchHistory
): ViewModel() {
    private val _searchInput = MutableLiveData<CharSequence>()
    val searchInput: LiveData<CharSequence> = _searchInput
    private val _searchResult = MutableLiveData<Result<List<Track>>>()
    val searchResult: LiveData<Result<List<Track>>> = _searchResult

    fun search(searchText: CharSequence) {
        searchUseCase(
            searchText = searchText,
            callback = { result ->
                _searchResult.value = result
            })
    }

    fun setupTextWatcher(): TextWatcher {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                _searchInput.value = text ?: ""
                if(searchInput.value != null)
                    search(searchInput.value!!)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        return textWatcher
    }
}