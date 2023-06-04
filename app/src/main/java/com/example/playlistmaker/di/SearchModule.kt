package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.data.network.RetrofitRemoteRepository
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.search.domain.usecase.*
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    viewModel {
        SearchViewModel(
            searchUseCase = get(),
            saveTrackUseCase = get(),
            getTrackListUseCase = get(),
            saveTrackListUseCase = get(),
            saveToHistoryUseCase = get(),
            clearSearchHistoryUseCase = get()
        )
    }

    single<TrackStorage> {
        SharedPrefsTrackStorage(context = get())
    }

    single<TrackRepository> {
        TrackRepositoryImpl(trackStorage = get())
    }

    single<TrackRepositoryRemote> {
        RetrofitRemoteRepository()
    }

    factory {
        Search(trackRepositoryRemote = get())
    }

    factory {
        GetTrack(trackRepository = get())
    }

    factory {
        GetTrackList(trackRepository = get())
    }

    factory {
        SaveTrack(trackRepository = get())
    }

    factory {
        SaveTrackList(trackRepository = get())
    }

    factory {
        SaveToHistory(trackRepository = get())
    }

    factory {
        ClearSearchHistory(trackRepository = get())
    }
}