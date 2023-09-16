package com.example.playlistmaker.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long?.asMinutesAndSeconds(): String = String.format("%1\$tM:%1\$tS", this)

fun String?.asYear(): String {
    return DateTimeFormatter
        .ofPattern("yyyy", Locale.getDefault())
        .withZone(ZoneId.systemDefault())
        .format(Instant.parse(this))
}
