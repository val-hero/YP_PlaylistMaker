package com.example.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsManager<T>(val sharedPreferences: SharedPreferences) {


    inline fun <reified T> itemToJson(item: T): String = Gson().toJson(item)
    inline fun <reified T> itemListToJson(items: ArrayList<T>): String = Gson().toJson(items)

    inline fun <reified T> jsonToItem(json: String): T =
        Gson().fromJson(json, T::class.java)

    inline fun <reified T> jsonToItemList(json: String): ArrayList<T> =
        Gson().fromJson(json, object : TypeToken<ArrayList<T>>() {}.type)


    inline fun <reified T> addItem(key: String, item: T) {
        sharedPreferences.edit { putString(key, itemToJson(item)) }
    }

    inline fun <reified T> addItemList(key: String, items: ArrayList<T>) {
        val json = Gson().toJson(items, object : TypeToken<ArrayList<T>>() {}.type)
        sharedPreferences.edit { putString(key, json) }
    }

    inline fun <reified T> getItems(key: String): ArrayList<T> {
        val json = sharedPreferences.getString(key, null) ?: return arrayListOf()
        return jsonToItemList(json)
    }

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}