package com.example.playlistmaker.settings.data

import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.utility.DARK_THEME_ENABLED
import com.example.playlistmaker.utility.SETTINGS_SHARED_PREFS

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {

    private var appThemeIsDark = false
    private val sharedPrefs by lazy {
        context.getSharedPreferences(SETTINGS_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    override fun changeAppTheme(currentThemeIsDark: Boolean) {
        appThemeIsDark = currentThemeIsDark
        sharedPrefs.edit { putBoolean(DARK_THEME_ENABLED, appThemeIsDark) }
    }

    override fun isDarkThemeEnabled(): Boolean {
        val systemThemeIsDark =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        appThemeIsDark = sharedPrefs.getBoolean(DARK_THEME_ENABLED, systemThemeIsDark)
        return appThemeIsDark
    }
}