package com.example.playlistmaker.library.favourite.domain.model

import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity

data class FavouriteTrack(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    val genre: String,
    val duration: Long,
    val imageUrl: String
)

fun FavouriteTrack.mapToEntity(): FavouriteTrackEntity {
    return FavouriteTrackEntity(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        country = this.country,
        previewUrl = this.previewUrl,
        genre = this.genre,
        duration = this.duration,
        imageUrl = this.imageUrl,
        insertedAt = 0L
    )
}