package com.example.playlistmaker.library.favourite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity

@Dao
interface FavouriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteTrackEntity: FavouriteTrackEntity)

    @Query("SELECT * FROM fav_tracks ORDER BY insertedAt DESC")
    suspend fun getAll(): List<FavouriteTrackEntity>

    @Query("SELECT trackId FROM fav_tracks")
    suspend fun getIds(): List<Long>

    @Query("DELETE FROM fav_tracks WHERE trackId == :id")
    suspend fun delete(id: Long)
}