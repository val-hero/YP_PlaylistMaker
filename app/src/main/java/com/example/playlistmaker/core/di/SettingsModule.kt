package com.example.playlistmaker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.GetCurrentDarkTheme
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import com.example.playlistmaker.utility.SETTINGS_SHARED_PREFS
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {
    viewModel {
        SettingsViewModel(
            changeAppThemeUseCase = get(),
            getCurrentDarkThemeUseCase = get(),
            openTermsUseCase = get(),
            shareAppUseCase = get(),
            sendMailToSupportUseCase = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            context = get(),
            sharedPreferences = get(qualifier = named(SETTINGS_SHARED_PREFS))
        )
    }

    single<SharedPreferences>(qualifier = named(SETTINGS_SHARED_PREFS)) {
        androidContext().getSharedPreferences(SETTINGS_SHARED_PREFS, Context.MODE_PRIVATE)
    }

    factory {
        ChangeAppTheme(settingsRepository = get())
    }

    factory {
        GetCurrentDarkTheme(settingsRepository = get())
    }
}
