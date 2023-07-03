package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.mapper.TrackDtoMapper
import com.example.playlistmaker.search.data.model.SearchResponse
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.repository.TrackRepositoryRemote
import com.example.playlistmaker.utility.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitRemoteRepository(
    private val api: ITunesApiService,
    private val mapper: TrackDtoMapper
) : TrackRepositoryRemote {

    override fun getTracks(expression: CharSequence, callback: (Result<ArrayList<Track>>) -> Unit) {
        api.getTracks(expression)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    val result = response.body()?.tracks
                    if (result.isNullOrEmpty())
                        callback(Result.Error(response.code()))
                    else
                        callback(Result.Success(result.map { mapper.mapToDomainModel(it) } as ArrayList))
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    callback(Result.Error(resultCode = -1))
                }
            }
            )
    }
}