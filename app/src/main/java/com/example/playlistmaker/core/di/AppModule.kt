package com.example.playlistmaker.core.di

import androidx.room.Room
import com.example.playlistmaker.core.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }
}