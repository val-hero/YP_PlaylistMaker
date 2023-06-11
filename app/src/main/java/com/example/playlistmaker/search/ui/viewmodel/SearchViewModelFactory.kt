package com.example.playlistmaker.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.data.network.RetrofitITunesRepository
import com.example.playlistmaker.search.domain.usecase.*

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val repository = Creator.provideTrackRepository(context)
    private val trackApi by lazy(LazyThreadSafetyMode.NONE) { RetrofitITunesRepository() }
    private val searchUseCase by lazy(LazyThreadSafetyMode.NONE) { Search(trackApi) }
    private val saveTrackListUseCase by lazy(LazyThreadSafetyMode.NONE) { SaveTrackList(repository) }
    private val getTrackListUseCase by lazy(LazyThreadSafetyMode.NONE) { GetTrackList(repository) }
    private val saveTrackUseCase by lazy(LazyThreadSafetyMode.NONE) { SaveTrack(repository) }
    private val saveToHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) { SaveToHistory(repository) }
    private val clearSearchHistoryUseCase by lazy(LazyThreadSafetyMode.NONE) { ClearSearchHistory(repository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            searchUseCase = searchUseCase,
            saveTrackListUseCase = saveTrackListUseCase,
            getTrackListUseCase = getTrackListUseCase,
            saveTrackUseCase = saveTrackUseCase,
            saveToHistoryUseCase = saveToHistoryUseCase,
            clearSearchHistoryUseCase = clearSearchHistoryUseCase
        ) as T
    }
}