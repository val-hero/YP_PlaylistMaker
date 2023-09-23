package com.example.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.library.favourite.data.dao.FavouriteTracksDao
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity
import com.example.playlistmaker.library.playlists.data.database.dao.PlaylistDao
import com.example.playlistmaker.library.playlists.data.database.entity.PlaylistEntity
import com.example.playlistmaker.search.data.database.dao.SearchHistoryDao
import com.example.playlistmaker.search.data.database.entity.SearchHistoryTrackEntity

@Database(
    version = 2,
    entities = [
        FavouriteTrackEntity::class,
        SearchHistoryTrackEntity::class,
        PlaylistEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTracksDao(): FavouriteTracksDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun playlistDao(): PlaylistDao
}