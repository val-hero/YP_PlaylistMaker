package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.api.TrackSearchApi
import com.example.playlistmaker.domain.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.playlistmaker.utility.Result

class RetrofitITunesApi : TrackSearchApi {

    override fun getTracks(searchText: CharSequence, callback: (Result<ArrayList<Track>>) -> Unit) {
        RetrofitClient.api.getTracks(searchText)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    if (response.code() != 200 || response.body()?.results.isNullOrEmpty()) {
                        callback(Result.NotFound("Track is not found"))
                    } else {
                        callback(Result.Success(response.body()?.results!!))
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    callback(Result.Error("Network error: ${t.message}"))
                }

            }
            )
    }
}