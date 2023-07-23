package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.mapper.TrackDtoMapper
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.utility.ErrorType
import com.example.playlistmaker.utility.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class RetrofitRemoteRepository(
    private val api: ITunesApiService,
    private val mapper: TrackDtoMapper
) : TrackRepositoryRemote {

    override suspend fun getTracks(expression: CharSequence): Flow<Result<List<Track>>> = flow {
        try {
            val tracks = api.getTracks(expression).results
            if (tracks.isEmpty()) {
                emit(Result.Error(ErrorType.NOT_FOUND))
            } else {
                emit(Result.Success(tracks.map { mapper.mapToDomainModel(it) }))
            }

        } catch (e: Exception) {
            when (e) {
                is HttpException, is IOException -> emit(Result.Error(ErrorType.NO_NETWORK_CONNECTION))
                else -> emit(Result.Error(ErrorType.UNEXPECTED_ERROR))
            }
        }
    }
}