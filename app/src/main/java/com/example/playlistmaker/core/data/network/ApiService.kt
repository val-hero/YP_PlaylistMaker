package com.example.playlistmaker.core.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search?entity=song")
    suspend fun getTracks(
        @Query("term") text: CharSequence
    ): ApiResponse

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }
}