package com.example.playlistmaker.library.favourite.data.repository

import android.os.SystemClock
import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.library.favourite.data.entity.mapToDomain
import com.example.playlistmaker.library.favourite.data.entity.mapToFavouriteEntity
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavouriteTracksRepository {

    override fun getAll(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favouriteTracksDao().getAll()
        emit(tracks.map { it.mapToDomain() })
    }

    override suspend fun save(track: Track) {
        appDatabase.favouriteTracksDao().insert(
            track.mapToFavouriteEntity().copy(insertedAt = SystemClock.uptimeMillis())
        )
    }

    override suspend fun delete(id: Long) {
        appDatabase.favouriteTracksDao().delete(id)
    }

    override fun getIds(): Flow<List<Long>> = flow {
        val trackIds = appDatabase.favouriteTracksDao().getIds()
        emit(trackIds)
    }
}