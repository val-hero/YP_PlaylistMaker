package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.model.Track

interface TrackStorage {

    fun save(track: Track)

    fun get(): Track

    fun saveList(tracks: ArrayList<Track>)

    fun getList(): ArrayList<Track>

    fun clear()

    fun delete()
}