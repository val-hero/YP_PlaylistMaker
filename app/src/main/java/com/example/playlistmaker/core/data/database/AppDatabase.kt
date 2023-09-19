package com.example.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.favourite.data.dao.FavouriteTracksDao
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity
import com.example.playlistmaker.search.data.database.dao.SearchHistoryDao
import com.example.playlistmaker.search.data.database.entity.SearchHistoryTrackEntity

@Database(
    version = 1,
    entities = [
        FavouriteTrackEntity::class,
        SearchHistoryTrackEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTracksDao(): FavouriteTracksDao

    abstract fun searchHistoryDao(): SearchHistoryDao
}