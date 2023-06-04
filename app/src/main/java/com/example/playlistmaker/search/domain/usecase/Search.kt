package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utility.Result

class Search(private val trackRepositoryRemote: TrackRepositoryRemote) {

    operator fun invoke(
        expression: CharSequence,
        callback: (Result<ArrayList<Track>>) -> Unit
    ) {
        return trackRepositoryRemote.getTracks(expression, callback)
    }
}