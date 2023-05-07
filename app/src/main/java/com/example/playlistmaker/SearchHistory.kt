package com.example.playlistmaker

class SearchHistory {
    val tracks = ArrayList<Track>()

    fun addTrack(track: Track) {
        removeDuplicate(track)
        if (tracks.size >= HISTORY_CAPACITY) {
            tracks.removeLast()
        }
        tracks.add(0, track)
    }

    private fun removeDuplicate(track: Track) {
        val iterator = tracks.iterator()
        while (iterator.hasNext()) {
            val currentTrack = iterator.next()
            if (track.trackId == currentTrack.trackId) {
                iterator.remove()
            }
        }
    }

    private companion object {
        private const val HISTORY_CAPACITY = 10
    }
}