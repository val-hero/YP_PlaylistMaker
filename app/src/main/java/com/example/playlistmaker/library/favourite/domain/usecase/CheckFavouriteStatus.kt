package com.example.playlistmaker.library.favourite.domain.usecase

import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CheckFavouriteStatus(private val favTracksRepository: FavouriteTracksRepository) {

    suspend operator fun invoke(trackId: Long): Flow<Boolean> = flow {
        favTracksRepository.getIds().collect {
            if (it.contains(trackId))
                emit(true)
            else
                emit(false)

        }
    }
}