package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.utility.DARK_THEME_ENABLED

class SettingsRepositoryImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : SettingsRepository {

    private var appThemeIsDark = false

    override fun changeAppTheme(currentThemeIsDark: Boolean) {
        appThemeIsDark = currentThemeIsDark
        sharedPreferences.edit { putBoolean(DARK_THEME_ENABLED, appThemeIsDark) }
    }

    override fun isDarkThemeEnabled(): Boolean {
        val systemThemeIsDark =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        appThemeIsDark = sharedPreferences.getBoolean(DARK_THEME_ENABLED, systemThemeIsDark)
        return appThemeIsDark
    }
}