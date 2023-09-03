package com.example.playlistmaker.core.data.network

import com.example.playlistmaker.core.domain.model.Track

data class TrackDto(
    val trackId: Long,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val country: String?,
    val previewUrl: String?,
    val primaryGenreName: String?,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?
)

fun TrackDto.mapToDomain(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName ?: "",
        artistName =  this.artistName ?: "",
        collectionName = this.collectionName ?: "",
        releaseDate = this.releaseDate ?: "",
        country = this.country ?: "",
        previewUrl = if(!this.previewUrl.isNullOrBlank()) this.previewUrl else "",
        genre = this.primaryGenreName ?: "",
        duration = this.trackTimeMillis ?: 0,
        imageUrl = this.artworkUrl100 ?: ""
    )
}
