package com.example.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.favourite.data.dao.FavouriteTracksDao
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity

@Database(version = 1, entities = [FavouriteTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun favouriteTracksDao(): FavouriteTracksDao
}