package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.search.domain.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.playlistmaker.utility.Result

class RetrofitRemoteRepository(private val api: ITunesApiService) : TrackRepositoryRemote {

    override fun getTracks(expression: CharSequence, callback: (Result<ArrayList<Track>>) -> Unit) {
        api.getTracks(expression)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    val result = response.body()?.results
                    if (result.isNullOrEmpty())
                        callback(Result.Error(response.code()))
                    else
                        callback(Result.Success(result))
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    callback(Result.Error(resultCode = -1))
                }
            }
            )
    }
}