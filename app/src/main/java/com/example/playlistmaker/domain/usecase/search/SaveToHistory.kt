package com.example.playlistmaker.domain.usecase.search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.usecase.SaveTrackList
import com.example.playlistmaker.utility.HISTORY_CAPACITY

class SaveToHistory(private val saveTrackListUseCase: SaveTrackList) {

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
        saveTrackListUseCase(history)
    }
}