package com.example.playlistmaker.utility

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val systemThemeIsDark =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val appTheme = getSharedPreferences(SETTINGS_SHARED_PREFS, Context.MODE_PRIVATE)
            .getBoolean(DARK_THEME_ENABLED, systemThemeIsDark)

        switchTheme(appTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }

}