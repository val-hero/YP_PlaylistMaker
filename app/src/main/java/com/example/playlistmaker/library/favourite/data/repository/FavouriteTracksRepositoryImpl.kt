package com.example.playlistmaker.library.favourite.data.repository

import android.os.SystemClock
import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.library.favourite.data.entity.mapToDomain
import com.example.playlistmaker.library.favourite.domain.model.FavouriteTrack
import com.example.playlistmaker.library.favourite.domain.model.mapToEntity
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavouriteTracksRepository {

    override fun getAll(): Flow<List<FavouriteTrack>> = flow {
        val tracks = appDatabase.favouriteTracksDao().getTracks()
        emit(tracks.map { it.mapToDomain() })
    }

    override suspend fun save(favouriteTrack: FavouriteTrack) {
        appDatabase.favouriteTracksDao().insertTrack(
            favouriteTrack.mapToEntity().copy(insertedAt = SystemClock.uptimeMillis())
        )
    }
}