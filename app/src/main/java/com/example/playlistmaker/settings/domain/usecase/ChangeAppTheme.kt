package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.SettingsRepository

class ChangeAppTheme(private val settingsRepository: SettingsRepository) {

    operator fun invoke(currentThemeIsDark: Boolean) {
        settingsRepository.changeAppTheme(currentThemeIsDark)
    }
}