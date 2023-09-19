package com.example.playlistmaker.core.domain.repository

import com.example.playlistmaker.core.domain.model.Track

interface TrackRepository {

    fun save(track: Track)

    fun get(): Track
}