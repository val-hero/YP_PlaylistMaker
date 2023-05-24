package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.storage.TrackStorage
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TrackRepository

class TrackRepositoryImpl(private val trackStorage: TrackStorage) : TrackRepository {

    override fun saveTrack(track: Track) {
        trackStorage.save(track)
    }

    override fun getTrack(): Track = trackStorage.get()

    override fun saveTrackList(tracks: List<Track>) {
        trackStorage.saveList(tracks)
    }

    override fun getTrackList(): List<Track> = trackStorage.getList()

    override fun delete() {
        trackStorage.delete()
    }

    override fun clear() {
        trackStorage.clear()
    }

}