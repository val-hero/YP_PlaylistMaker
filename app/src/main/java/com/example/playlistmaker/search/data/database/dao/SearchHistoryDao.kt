package com.example.playlistmaker.search.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.search.data.database.entity.SearchHistoryTrackEntity

@Dao
abstract class SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(searchHistoryTrackEntity: SearchHistoryTrackEntity)

    @Query("SELECT * FROM search_history ORDER BY insertedAt DESC")
    abstract suspend fun getAll(): List<SearchHistoryTrackEntity>

    @Query("SELECT COUNT(trackId) FROM search_history")
    abstract suspend fun getCount(): Int

    @Query("DELETE FROM search_history WHERE insertedAt = " +
            "(SELECT MIN(insertedAt) FROM search_history)")
    abstract suspend fun deleteOldest()

    @Query("DELETE FROM search_history")
    abstract fun clear()
}