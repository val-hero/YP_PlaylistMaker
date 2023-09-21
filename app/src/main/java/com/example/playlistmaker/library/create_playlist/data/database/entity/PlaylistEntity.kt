package com.example.playlistmaker.library.create_playlist.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.library.create_playlist.domain.model.Playlist

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String,
    val imageUri: String,
    val insertedAt: Long?
)

fun PlaylistEntity.mapToDomain(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUri = this.imageUri
    )
}

fun Playlist.mapToPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUri = this.imageUri,
        insertedAt = null
    )
}