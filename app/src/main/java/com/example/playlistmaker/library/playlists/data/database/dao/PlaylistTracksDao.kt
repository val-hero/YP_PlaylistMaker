package com.example.playlistmaker.library.playlists.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.library.playlists.data.database.entity.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trackEntity: PlaylistTrackEntity)

    @Update
    suspend fun update(track: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun get(trackId: Long): PlaylistTrackEntity

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (:trackIds) ORDER BY insertedAt DESC")
    suspend fun getByIds(trackIds: ArrayList<Long>): List<PlaylistTrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun delete(trackId: Long)
}