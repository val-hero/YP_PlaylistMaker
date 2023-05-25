package com.example.playlistmaker.presentation.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.network.RetrofitITunesApi
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.data.storage.local.SharedPrefsTrackStorage
import com.example.playlistmaker.domain.usecase.*
import com.example.playlistmaker.domain.usecase.search.ClearSearchHistory
import com.example.playlistmaker.domain.usecase.search.Search

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val trackStorage by lazy(LazyThreadSafetyMode.NONE) { SharedPrefsTrackStorage(context) }
    private val trackRepository by lazy(LazyThreadSafetyMode.NONE) { TrackRepositoryImpl(trackStorage) }
    private val trackApi by lazy(LazyThreadSafetyMode.NONE) { RetrofitITunesApi() }
    private val searchUseCase by lazy(LazyThreadSafetyMode.NONE) { Search(trackApi) }
    private val saveTrackListUseCase by lazy(LazyThreadSafetyMode.NONE) { SaveTrackList(trackRepository) }
    private val getTrackListUseCase by lazy(LazyThreadSafetyMode.NONE) { GetTrackList(trackRepository) }
    private val saveTrackUseCase by lazy(LazyThreadSafetyMode.NONE) { SaveTrack(trackRepository) }
    private val clearSearchHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) { ClearSearchHistory(trackRepository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchUseCase = searchUseCase,
            saveTrackListUseCase = saveTrackListUseCase,
            getTrackListUseCase = getTrackListUseCase,
            saveTrackUseCase = saveTrackUseCase,
            clearSearchHistoryUseCase = clearSearchHistoryUseCase
        ) as T
    }
}