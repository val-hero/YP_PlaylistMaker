package com.example.playlistmaker.library.favourite.domain.repository

import com.example.playlistmaker.library.favourite.domain.model.FavouriteTrack
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {

    fun getAll(): Flow<List<FavouriteTrack>>

    suspend fun save(favouriteTrack: FavouriteTrack)
}