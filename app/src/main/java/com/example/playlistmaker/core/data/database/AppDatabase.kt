package com.example.playlistmaker.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.core.utils.Converters
import com.example.playlistmaker.library.favourite.data.dao.FavouriteTracksDao
import com.example.playlistmaker.library.favourite.data.entity.FavouriteTrackEntity
import com.example.playlistmaker.library.playlists.data.database.dao.PlaylistDao
import com.example.playlistmaker.library.playlists.data.database.dao.PlaylistTracksDao
import com.example.playlistmaker.library.playlists.data.database.entity.PlaylistEntity
import com.example.playlistmaker.library.playlists.data.database.entity.PlaylistTrackEntity
import com.example.playlistmaker.search.data.database.dao.SearchHistoryDao
import com.example.playlistmaker.search.data.database.entity.SearchHistoryTrackEntity

@Database(
    version = 1,
    entities = [
        FavouriteTrackEntity::class,
        SearchHistoryTrackEntity::class,
        PlaylistEntity::class,
        PlaylistTrackEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTracksDao(): FavouriteTracksDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao
}