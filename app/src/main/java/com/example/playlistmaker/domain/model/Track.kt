package com.example.playlistmaker.domain.model

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class Track(
    val trackName: String,
    val artistName: String,
    val trackId: Long,
    val collectionName: String,
    val releaseDate: String,
    val country: String,
    val previewUrl: String,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("trackTimeMillis") val duration: Long,
    @SerializedName("artworkUrl100") val imageUrl: String
) {
    fun resizedImage() = imageUrl.replaceAfterLast("/", "512x512bb.jpg")

    fun releaseYear(): String {
        return DateTimeFormatter
            .ofPattern("yyyy", Locale.getDefault())
            .withZone(ZoneId.systemDefault())
            .format(Instant.parse(releaseDate))
    }

    fun formattedDuration(): String {
        return String.format("%1\$tM:%1\$tS", duration)
    }
}
