package com.example.playlistmaker.core.utility

fun Long?.asMinutesAndSeconds(): String = String.format("%1\$tM:%1\$tS", this)
