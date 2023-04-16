package com.example.playlistmaker

class SearchHistory {
    companion object {
        const val HISTORY_CAPACITY = 10
    }

    val tracks = ArrayList<Track>()

    fun addTrack(track: Track) {
        removeDuplicate(track)
        if (tracks.size >= HISTORY_CAPACITY) {
            tracks.removeLast()
        }
        tracks.add(0, track)
    }

    private fun removeDuplicate(track: Track) {
        tracks.forEachIndexed { index, comparable ->
            if (track.trackId == comparable.trackId) {
                tracks.removeAt(index)
            }
        }
    }
}