package com.example.playlistmaker.data.network

import com.example.playlistmaker.domain.model.Track

data class SearchResponse(val results: ArrayList<Track>)