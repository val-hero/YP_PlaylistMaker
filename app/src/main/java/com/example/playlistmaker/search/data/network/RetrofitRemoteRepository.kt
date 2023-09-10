package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.core.utils.ErrorType
import com.example.playlistmaker.core.utils.Result
import com.example.playlistmaker.search.data.model.mapToDomain
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RetrofitRemoteRepository(
    private val api: ITunesApiService,
) : TrackRepositoryRemote {

    override suspend fun getTracks(expression: CharSequence): Flow<Result<List<Track>>> = flow {
        try {
            val tracks = api.getTracks(expression).results
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
}