package com.example.playlistmaker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.core.utils.TRACKS_SHARED_PREFS
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.RetrofitRemoteRepository
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetTrack
import com.example.playlistmaker.search.domain.usecase.GetTrackList
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
import com.example.playlistmaker.search.domain.usecase.SaveTrack
import com.example.playlistmaker.search.domain.usecase.SaveTrackList
import com.example.playlistmaker.search.domain.usecase.Search
import com.example.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        SharedPrefsTrackStorage(
            sharedPreferences = get(qualifier = named(TRACKS_SHARED_PREFS))
        )
    }

    single<TrackRepository> {
        TrackRepositoryImpl(trackStorage = get())
    }

    single<TrackRepositoryRemote> {
        RetrofitRemoteRepository(api = get())
    }

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl(ITunesApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<SharedPreferences>(qualifier = named(TRACKS_SHARED_PREFS)) {
        androidContext().getSharedPreferences(TRACKS_SHARED_PREFS, Context.MODE_PRIVATE)
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