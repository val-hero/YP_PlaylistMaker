package com.example.playlistmaker.library.favourite.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.core.domain.model.Track

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
    val insertedAt: Long?,
    val playlistIds: ArrayList<Long>?
)

fun FavouriteTrackEntity.mapToDomain(): Track {
    return Track(
        id = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        country = this.country,
        previewUrl = this.previewUrl,
        genre = this.genre,
        duration = this.duration,
        imageUrl = this.imageUrl,
        playlistIds = this.playlistIds ?: arrayListOf()
    )
}

fun Track.mapToFavouriteEntity(): FavouriteTrackEntity {
    return FavouriteTrackEntity(
        trackId = this.id,
        trackName = this.trackName,
        artistName = this.artistName,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        country = this.country,
        previewUrl = this.previewUrl,
        genre = this.genre,
        duration = this.duration,
        imageUrl = this.imageUrl,
        insertedAt = null,
        playlistIds = this.playlistIds
    )
}