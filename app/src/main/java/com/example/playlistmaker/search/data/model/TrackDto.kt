package com.example.playlistmaker.search.data.model

data class TrackDto(
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    val primaryGenreName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
