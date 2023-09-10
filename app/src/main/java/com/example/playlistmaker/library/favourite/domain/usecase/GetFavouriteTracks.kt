package com.example.playlistmaker.library.favourite.domain.usecase

import com.example.playlistmaker.library.favourite.domain.repository.FavouriteTracksRepository

class GetFavouriteTracks(private val favTracksRepository: FavouriteTracksRepository) {

    operator fun invoke() = favTracksRepository.getAll()
}