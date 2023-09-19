package com.example.playlistmaker.core.domain.model

data class Track(
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    val genre: String,
    val duration: Long,
    val imageUrl: String,
    val isFavourite: Boolean = false
)

