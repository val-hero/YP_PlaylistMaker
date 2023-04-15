package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {
    companion object {
        const val SETTINGS_PREFS = "settings"
        const val DARK_THEME_ENABLED = "dark_theme_enabled"
    }

    var systemThemeIsDark = false
    var appThemeIsDark = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        switchTheme(darkThemeOn())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        appThemeIsDark = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
        sharedPrefs.edit { putBoolean(DARK_THEME_ENABLED, appThemeIsDark) }
    }

    private fun darkThemeOn(): Boolean {
        systemThemeIsDark =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        sharedPrefs = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE)
        appThemeIsDark = sharedPrefs.getBoolean(DARK_THEME_ENABLED, systemThemeIsDark)
        return appThemeIsDark
    }
}