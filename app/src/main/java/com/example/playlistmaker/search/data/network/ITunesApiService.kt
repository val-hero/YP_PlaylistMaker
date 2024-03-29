package com.example.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun getTracks(
        @Query("term") text: CharSequence
    ): SearchResponse

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}