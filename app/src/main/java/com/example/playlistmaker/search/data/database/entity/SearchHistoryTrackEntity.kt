package com.example.playlistmaker.search.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.core.domain.model.Track

@Entity(tableName = "search_history")
data class SearchHistoryTrackEntity(
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
    val insertedAt: Long?
)

fun SearchHistoryTrackEntity.mapToDomain(): Track {
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
        imageUrl = this.imageUrl
    )
}

fun Track.toSearchHistoryTrackEntity(): SearchHistoryTrackEntity {
    return SearchHistoryTrackEntity(
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
        insertedAt = null
    )
}