package com.example.playlistmaker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.core.domain.usecase.GetSelectedTrack
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.core.utils.TRACKS_SHARED_PREFS
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.domain.usecase.ClearSearchHistory
import com.example.playlistmaker.search.domain.usecase.GetSearchHistory
import com.example.playlistmaker.search.domain.usecase.SaveToHistory
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
            saveToHistoryUseCase = get(),
            clearSearchHistoryUseCase = get(),
            getSearchHistoryUseCase = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(api = get(), database = get())
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
        Search(searchRepository = get())
    }

    factory {
        GetSelectedTrack(trackRepository = get())
    }

    factory {
        SaveTrack(trackRepository = get())
    }

    factory {
        GetSearchHistory(searchRepository = get())
    }

    factory {
        SaveToHistory(searchRepository = get())
    }

    factory {
        ClearSearchHistory(searchRepository = get())
    }
}