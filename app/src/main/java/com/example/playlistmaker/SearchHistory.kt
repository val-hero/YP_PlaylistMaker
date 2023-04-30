package com.example.playlistmaker

class SearchHistory {
    private companion object {
        private const val HISTORY_CAPACITY = 10
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
        tracks.forEachIndexed { _, comparable ->
            tracks.removeIf { track.trackId == comparable.trackId }
        }
    }
}