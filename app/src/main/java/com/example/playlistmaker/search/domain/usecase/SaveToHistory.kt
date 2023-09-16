package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.core.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository

class SaveToHistory(private val trackRepository: TrackRepository) {

    operator fun invoke(track: Track, history: ArrayList<Track>) {
        val iterator = history.iterator()
        while (iterator.hasNext()) {
            val currentTrack = iterator.next()
            if (track.trackId == currentTrack.trackId) {
                iterator.remove()
            }
        }
        if (history.size >= HISTORY_CAPACITY) {
            history.removeLast()
        }
        history.add(0, track)
        trackRepository.saveTrackList(history)
    }

    companion object {
        const val HISTORY_CAPACITY = 10
    }
}