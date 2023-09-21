package com.example.playlistmaker.library.create_playlist.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.create_playlist.data.database.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAll(): List<PlaylistEntity>

}