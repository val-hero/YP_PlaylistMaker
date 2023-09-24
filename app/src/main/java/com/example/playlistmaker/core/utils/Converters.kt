package com.example.playlistmaker.core.utils

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun longsToString(value: List<Long>) = JsonConverter.itemToJson(value)

    @TypeConverter
    fun stringToLongs(value: String) = JsonConverter.jsonToItemList<Long>(value)
}