package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    @SerializedName("trackTimeMillis") val duration: Long,
    @SerializedName("artworkUrl100") val imageUrl: String
)
