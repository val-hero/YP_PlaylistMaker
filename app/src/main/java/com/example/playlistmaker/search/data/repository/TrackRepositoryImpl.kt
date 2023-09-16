package com.example.playlistmaker.search.data.repository

import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.repository.TrackRepository

class TrackRepositoryImpl(private val trackStorage: TrackStorage) : TrackRepository {

    override fun saveTrack(track: Track) {
        trackStorage.save(track)
    }

    override fun getTrack(): Track = trackStorage.get()

    override fun saveTrackList(tracks: ArrayList<Track>) {
        trackStorage.saveList(tracks)
    }

    override fun getTrackList(): ArrayList<Track> = trackStorage.getList()

    override fun delete() {
        trackStorage.delete()
    }

    override fun clear() {
        trackStorage.clear()
    }

}