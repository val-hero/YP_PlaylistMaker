package com.example.playlistmaker.data.storage

import android.content.Context
import androidx.core.content.edit
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utility.JsonConverter
import com.example.playlistmaker.utility.SELECTED_TRACK
import com.example.playlistmaker.utility.TRACKS_SHARED_PREFS
import com.example.playlistmaker.utility.TRACK_LIST

class SharedPrefsTrackStorage(context: Context) : TrackStorage {

    private val sharedPrefs = context.getSharedPreferences(TRACKS_SHARED_PREFS, Context.MODE_PRIVATE)

    override fun save(track: Track) {
        sharedPrefs.edit { putString(SELECTED_TRACK, JsonConverter.itemToJson(track)) }
    }

    override fun get(): Track {
        val json = sharedPrefs.getString(SELECTED_TRACK, "") ?: ""
        return JsonConverter.jsonToItem(json)
    }


    override fun saveList(tracks: ArrayList<Track>) {
        sharedPrefs.edit { putString(TRACK_LIST, JsonConverter.itemListToJson(tracks)) }
    }

    override fun getList(): ArrayList<Track> {
        val json = sharedPrefs.getString(TRACK_LIST, null) ?: return arrayListOf()
        return JsonConverter.jsonToItemList(json)
    }

    override fun clear() {
        sharedPrefs.edit { clear() }
    }

    override fun delete() {
        sharedPrefs.edit { remove(SELECTED_TRACK)}
    }
}