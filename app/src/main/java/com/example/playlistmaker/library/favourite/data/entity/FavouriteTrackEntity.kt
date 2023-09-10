package com.example.playlistmaker.library.favourite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.library.favourite.domain.model.FavouriteTrack

@Entity(tableName = "fav_tracks")
data class FavouriteTrackEntity(
    @PrimaryKey val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    val genre: String,
    val duration: Long,
    val imageUrl: String,
    val insertedAt: Long
)

fun FavouriteTrackEntity.mapToDomain(): FavouriteTrack {
    return FavouriteTrack(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        country = this.country,
        previewUrl = this.previewUrl,
        genre = this.genre,
        duration = this.duration,
        imageUrl = this.imageUrl
    )
}