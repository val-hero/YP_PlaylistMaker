package com.example.playlistmaker.core.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.core.domain.model.Track
import com.example.playlistmaker.core.domain.repository.TrackRepository
import com.example.playlistmaker.core.utils.JsonConverter
import com.example.playlistmaker.core.utils.SELECTED_TRACK

class TrackRepositoryImpl(private val sharedPreferences: SharedPreferences) : TrackRepository {

    override fun save(track: Track) {
        sharedPreferences.edit {
            putString(
                SELECTED_TRACK,
                JsonConverter.itemToJson(track)
            )
        }
    }

    override fun get(): Track {
        val json = sharedPreferences.getString(SELECTED_TRACK, "") ?: ""
        return JsonConverter.jsonToItem(json)
    }
}