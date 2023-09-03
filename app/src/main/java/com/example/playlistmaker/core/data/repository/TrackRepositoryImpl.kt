package com.example.playlistmaker.core.data.repository

import com.example.playlistmaker.core.data.network.ApiService
import com.example.playlistmaker.core.data.network.mapToDomain
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.repository.TrackRepository
import com.example.playlistmaker.core.utility.ErrorType
import com.example.playlistmaker.core.utility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class TrackRepositoryImpl(
    private val api: ApiService
) : TrackRepository {

    override fun save(track: Track) {

    }

    override fun saveList(tracks: List<Track>) {

    }

    override fun getById(id: Long): Resource<Track> {
        return Resource.Loading
    }

    override fun getByTerm(term: String) = flow {
        emit(Resource.Loading)

        try {
            val tracks = api.getTracks(term).results
            if (tracks.isEmpty()) {
                emit(Resource.Error(ErrorType.NOT_FOUND))
            } else {
                emit(Resource.Success(tracks.map { it.mapToDomain() }))
            }

        } catch (e: Exception) {
            when (e) {
                is HttpException, is IOException -> emit(Resource.Error(ErrorType.NO_NETWORK_CONNECTION))
                else -> emit(Resource.Error(ErrorType.UNEXPECTED_ERROR))
            }
        }
    }

    override fun getList(): Flow<Resource<List<Track>>> = flow {
        emit(Resource.Loading)
    }

    override fun delete() {

    }

    override fun clear() {

    }


}