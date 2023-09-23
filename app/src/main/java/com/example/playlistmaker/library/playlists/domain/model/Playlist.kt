package com.example.playlistmaker.library.playlists.domain.model

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val image: String?,
    val tracksCount: Int = 0
)