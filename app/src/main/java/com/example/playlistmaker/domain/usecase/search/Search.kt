package com.example.playlistmaker.domain.usecase.search

import com.example.playlistmaker.domain.api.TrackSearchApi
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.utility.Result

class Search(private val trackApi: TrackSearchApi) {

    operator fun invoke(
        searchText: CharSequence,
        callback: (Result<List<Track>>) -> Unit
    ) {
        return trackApi.getTracks(searchText, callback)
    }
}