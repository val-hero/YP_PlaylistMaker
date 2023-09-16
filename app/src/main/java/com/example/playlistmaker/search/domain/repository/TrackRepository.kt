package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.core.model.Track

interface TrackRepository {
    fun saveTrack(track: Track)
    fun getTrack(): Track
    fun saveTrackList(tracks: ArrayList<Track>)
    fun getTrackList(): ArrayList<Track>
    fun delete()
    fun clear()
}