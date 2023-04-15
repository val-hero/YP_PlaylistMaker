package com.example.playlistmaker.utility

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsEditor(val sharedPreferences: SharedPreferences) {

    inline fun <reified T> addItem(key: String, item: T) {
        sharedPreferences.edit { putString(key, JsonConverter.itemToJson(item)) }
    }

    inline fun <reified T> addItemList(key: String, items: ArrayList<T>) {
        val json = Gson().toJson(items, object : TypeToken<ArrayList<T>>() {}.type)
        sharedPreferences.edit { putString(key, json) }
    }

    inline fun <reified T> getItems(key: String): ArrayList<T> {
        val json = sharedPreferences.getString(key, null) ?: return arrayListOf()
        return JsonConverter.jsonToItemList(json)
    }

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}