package com.example.playlistmaker.data.storage

import com.example.playlistmaker.domain.models.Track

interface TrackStorage {

    fun save(track: Track)

    fun get(): Track

    fun saveList(tracks: ArrayList<Track>)

    fun getList(): ArrayList<Track>

    fun clear()

    fun delete()
}