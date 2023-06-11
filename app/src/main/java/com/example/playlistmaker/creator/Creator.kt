package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.search.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.search.data.storage.SharedPrefsTrackStorage
import com.example.playlistmaker.search.domain.repository.TrackRepository

object Creator {
    fun provideTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(SharedPrefsTrackStorage(context))
    }
}