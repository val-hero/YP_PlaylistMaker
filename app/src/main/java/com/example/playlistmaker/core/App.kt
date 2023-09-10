package com.example.playlistmaker.core

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.core.di.appModule
import com.example.playlistmaker.core.di.libraryModule
import com.example.playlistmaker.core.di.playerModule
import com.example.playlistmaker.core.di.searchModule
import com.example.playlistmaker.core.di.settingsModule
import com.example.playlistmaker.core.di.sharingModule
import com.example.playlistmaker.settings.domain.usecase.GetCurrentDarkTheme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    private val getCurrentDarkTheme: GetCurrentDarkTheme by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    playerModule,
                    searchModule,
                    settingsModule,
                    sharingModule,
                    libraryModule
                )
            )
        }

        switchTheme(getCurrentDarkTheme())
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