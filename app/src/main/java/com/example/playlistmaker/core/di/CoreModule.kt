package com.example.playlistmaker.core.di

import androidx.room.Room
import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.core.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.core.domain.repository.TrackRepository
import com.example.playlistmaker.core.domain.usecase.GetSelectedTrack
import com.example.playlistmaker.core.domain.usecase.SaveTrack
import com.example.playlistmaker.core.utils.TRACKS_SHARED_PREFS
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val coreModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database_vh.db")
            .build()
    }

    single<TrackRepository> {
        TrackRepositoryImpl(sharedPreferences = get(qualifier = named(TRACKS_SHARED_PREFS)))
    }

    factory {
        GetSelectedTrack(trackRepository = get())
    }

    factory {
        SaveTrack(trackRepository = get())
    }
}