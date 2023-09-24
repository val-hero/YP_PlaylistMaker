package com.example.playlistmaker.library.playlists.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.playlists.data.database.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    fun insert(trackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (SELECT trackIds FROM playlists WHERE id = :playlistId)")
    fun getByPlaylistId(playlistId: Long): List<PlaylistTrackEntity>
}