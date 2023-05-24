package com.example.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun getTracks(@Query("term") text:CharSequence): Call<SearchResponse>
}