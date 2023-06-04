package com.example.playlistmaker.utility

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val systemThemeIsDark =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val appTheme = getSharedPreferences(SETTINGS_SHARED_PREFS, Context.MODE_PRIVATE)
            .getBoolean(DARK_THEME_ENABLED, systemThemeIsDark)

        switchTheme(appTheme)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(listOf(playerModule, searchModule, settingsModule, sharingModule))
        }
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