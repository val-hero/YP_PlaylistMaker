package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"
    }

    var systemThemeIsDark = false
    var darkThemeOn = false
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(getString(R.string.sharedPrefsKey), MODE_PRIVATE)
        darkThemeOn = sharedPrefs.getBoolean(DARK_THEME_ENABLED, false)
        switchTheme(darkThemeOn)

        systemThemeIsDark =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkThemeOn = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_ENABLED, darkThemeOn)
            .apply()
    }
}