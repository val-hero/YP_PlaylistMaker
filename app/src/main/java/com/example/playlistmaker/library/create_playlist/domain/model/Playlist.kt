package com.example.playlistmaker.library.create_playlist.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val imageUri: String
)