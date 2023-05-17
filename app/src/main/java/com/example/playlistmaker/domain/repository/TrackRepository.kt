package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun saveTrack(track: Track)
    fun getTrack(): Track
    fun saveTrackList(tracks: ArrayList<Track>)
    fun getTrackList(): ArrayList<Track>
    fun delete()
    fun clear()
}