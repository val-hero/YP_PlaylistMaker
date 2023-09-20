package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.core.data.database.AppDatabase
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.core.utils.FlowResults
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.search.data.database.entity.mapToDomain
import com.example.playlistmaker.search.data.database.entity.toSearchHistoryTrackEntity
import com.example.playlistmaker.search.data.network.ITunesApiService
import com.example.playlistmaker.search.data.network.mapToDomain
import com.example.playlistmaker.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant

class SearchRepositoryImpl(
    private val api: ITunesApiService,
    private val database: AppDatabase
) : SearchRepository {

    private val dao = database.searchHistoryDao()

    override suspend fun search(query: String): FlowResults<Track> = flow {
        try {
            val tracks = api.getTracks(query).results
            if (tracks.isEmpty()) {
                emit(Result.Error(ErrorType.NOT_FOUND))
            } else {
                emit(Result.Success(tracks.map { it.mapToDomain() }))
            }

        } catch (e: Exception) {
            when (e) {
                is HttpException, is IOException -> emit(Result.Error(ErrorType.NO_NETWORK_CONNECTION))
                else -> emit(Result.Error(ErrorType.UNEXPECTED_ERROR))
            }
        }
    }

    override suspend fun saveToHistory(track: Track) {
        dao.insert(track.toSearchHistoryTrackEntity().copy(insertedAt = Instant.now().epochSecond))

        if (dao.getCount() > HISTORY_CAPACITY) {
            dao.deleteOldest()
        }
    }

    override suspend fun getHistory(): Flow<List<Track>> = flow {
        val history = dao.getAll()
        emit(history.map { it.mapToDomain() })
    }

    override fun clearHistory() {
        dao.clear()
    }

    companion object {
        const val HISTORY_CAPACITY = 10
    }
}