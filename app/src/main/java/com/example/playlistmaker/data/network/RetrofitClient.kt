package com.example.playlistmaker.data.network

import com.example.playlistmaker.utility.ITUNES_API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ITUNES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ITunesApi by lazy {
        client.create(ITunesApi::class.java)
    }
}