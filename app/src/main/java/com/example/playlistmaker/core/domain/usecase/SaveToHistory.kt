package com.example.playlistmaker.core.domain.usecase

import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.repository.TrackRepository

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
        trackRepository.saveList(history)
    }

    companion object {
        const val HISTORY_CAPACITY = 10
    }
}