package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.core.utils.JsonConverter
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.model.Track

class SharedPrefsTrackStorage(private val sharedPreferences: SharedPreferences) : TrackStorage {

    override fun save(track: Track) {
        sharedPreferences.edit { putString(SELECTED_TRACK, JsonConverter.itemToJson(track)) }
    }

    override fun get(): Track {
        val json = sharedPreferences.getString(SELECTED_TRACK, "") ?: ""
        return JsonConverter.jsonToItem(json)
    }


    override fun saveList(tracks: ArrayList<Track>) {
        sharedPreferences.edit { putString(TRACK_LIST, JsonConverter.itemListToJson(tracks)) }
    }

    override fun getList(): ArrayList<Track> {
        val json = sharedPreferences.getString(TRACK_LIST, null) ?: return arrayListOf()
        return JsonConverter.jsonToItemList(json)
    }

    override fun clear() {
        sharedPreferences.edit { clear() }
    }

    override fun delete() {
        sharedPreferences.edit { remove(SELECTED_TRACK)}
    }

    companion object {
        const val SELECTED_TRACK = "selected_track"
        const val TRACK_LIST = "track_list"
    }
}