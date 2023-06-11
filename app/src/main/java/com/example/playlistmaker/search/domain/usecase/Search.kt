package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.domain.repository.SearchRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utility.Result

class Search(private val trackApi: SearchRepository) {

    operator fun invoke(
        expression: CharSequence,
        callback: (Result<ArrayList<Track>>) -> Unit
    ) {
        return trackApi.getTracks(expression, callback)
    }
}