package com.example.playlistmaker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.core.data.network.ApiService
import com.example.playlistmaker.core.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.core.domain.repository.TrackRepository
import com.example.playlistmaker.core.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.core.domain.usecase.GetTrack
import com.example.playlistmaker.core.domain.usecase.GetTrackList
import com.example.playlistmaker.core.domain.usecase.SaveToHistory
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.core.domain.usecase.SaveTrackList
import com.example.playlistmaker.core.domain.usecase.Search
import com.example.playlistmaker.core.utility.TRACKS_SHARED_PREFS
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

//    single<TrackStorage> {
//        SharedPrefsTrackStorage(
//            sharedPreferences = get(qualifier = named(TRACKS_SHARED_PREFS))
//        )
//    }

    single<TrackRepository> {
        TrackRepositoryImpl(api = get())
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
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