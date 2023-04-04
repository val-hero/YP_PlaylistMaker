package com.example.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track(
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMilllis") val duration: String,
    @SerializedName("artworkUrl100") val imageUrl: String
)
