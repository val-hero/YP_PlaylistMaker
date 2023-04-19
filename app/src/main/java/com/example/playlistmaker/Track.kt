package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Track(
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: Date,
    val country: String,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("trackTimeMillis") val duration: Long,
    @SerializedName("artworkUrl100") val imageUrl: String
) {
    fun getResizedImage() = imageUrl.replaceAfterLast("/", "512x512bb.jpg")
}
