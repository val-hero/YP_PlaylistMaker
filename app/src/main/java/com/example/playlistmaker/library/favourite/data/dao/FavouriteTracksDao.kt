package com.example.playlistmaker.library.favourite.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity

@Dao
interface FavouriteTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(favouriteTrackEntity: FavouriteTrackEntity)

    @Query("SELECT * FROM fav_tracks")
    suspend fun getTracks(): List<FavouriteTrackEntity>
}