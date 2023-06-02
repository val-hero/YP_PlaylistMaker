package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.SettingsRepository

class GetCurrentDarkTheme(private val settingsRepository: SettingsRepository) {
    operator fun invoke(): Boolean {
        return settingsRepository.isDarkThemeEnabled()
    }
}