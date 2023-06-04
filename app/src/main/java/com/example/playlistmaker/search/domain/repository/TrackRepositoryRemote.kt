package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.utility.Result
import com.example.playlistmaker.search.domain.model.Track

interface TrackRepositoryRemote {
    fun getTracks(
        expression: CharSequence,
        callback: (Result<ArrayList<Track>>) -> Unit
    )
}