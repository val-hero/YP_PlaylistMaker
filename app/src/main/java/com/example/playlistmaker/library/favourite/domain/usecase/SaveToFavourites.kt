package com.example.playlistmaker.library.favourite.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository

class SaveToFavourites(private val favTracksRepository: FavouriteTracksRepository) {

    suspend operator fun invoke(track: Track) = favTracksRepository.save(track)
}