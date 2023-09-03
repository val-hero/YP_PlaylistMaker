package com.example.playlistmaker.core.domain.repository

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.utility.Resource
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun save(track: Track)

    fun saveList(tracks: List<Track>)

    fun getById(id: Long): Resource<Track>

    fun getByTerm(term: String): Flow<Resource<List<Track>>>

    fun getList(): Flow<Resource<List<Track>>>

    fun delete()

    fun clear()
}