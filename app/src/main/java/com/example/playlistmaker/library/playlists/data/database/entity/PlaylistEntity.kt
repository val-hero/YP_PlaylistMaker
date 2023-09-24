package com.example.playlistmaker.library.playlists.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.library.playlists.domain.model.Playlist

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String,
    val image: String?,
    val insertedAt: Long?,
    val tracksCount: Int?,
    val trackIds: ArrayList<Long>?
)

fun PlaylistEntity.mapToDomain(): Playlist {
    return Playlist(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image ?: "",
        tracksCount = this.tracksCount ?: 0,
        tracksIds = this.trackIds ?: arrayListOf()
    )
}

fun Playlist.mapToPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image,
        insertedAt = null,
        tracksCount = this.tracksCount,
        trackIds = this.tracksIds
    )
}