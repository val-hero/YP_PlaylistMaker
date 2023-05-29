package com.example.playlistmaker.domain.api

import com.example.playlistmaker.utility.Result
import com.example.playlistmaker.domain.model.Track

interface TrackSearchApi {
    fun getTracks(
        searchText: CharSequence,
        callback: (Result<ArrayList<Track>>) -> Unit
    )
}