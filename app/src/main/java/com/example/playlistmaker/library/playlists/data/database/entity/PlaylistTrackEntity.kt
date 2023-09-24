package com.example.playlistmaker.library.playlists.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.core.domain.model.Track

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity(
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

fun PlaylistTrackEntity.mapToDomain(): Track {
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

fun Track.mapToPlaylistTrackEntity(): PlaylistTrackEntity {
    return PlaylistTrackEntity(
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
