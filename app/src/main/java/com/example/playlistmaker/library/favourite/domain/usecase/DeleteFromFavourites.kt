package com.example.playlistmaker.library.favourite.domain.usecase

import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository

class DeleteFromFavourites(private val favTracksRepository: FavouriteTracksRepository) {

    suspend operator fun invoke(trackId: Long) = favTracksRepository.delete(trackId)
}