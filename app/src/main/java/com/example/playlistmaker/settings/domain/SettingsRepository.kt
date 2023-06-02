package com.example.playlistmaker.settings.domain

interface SettingsRepository {

    fun changeAppTheme(currentThemeIsDark: Boolean)

    fun isDarkThemeEnabled(): Boolean
}