package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search?entity=song")
    fun getTracks(
        @Query("term") text: CharSequence
    ): Call<SearchResponse>

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}