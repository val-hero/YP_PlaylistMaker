package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.model.Track

interface TrackStorage {

    fun save(track: Track)

    fun get(): Track

    fun saveList(tracks: List<Track>)

    fun getList(): List<Track>

    fun clear()

    fun delete()
}