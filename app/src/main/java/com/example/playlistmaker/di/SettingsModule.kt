package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.GetCurrentDarkTheme
import com.example.playlistmaker.settings.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
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
        SettingsRepositoryImpl(context = get())
    }

    factory {
        ChangeAppTheme(settingsRepository = get())
    }

    factory {
        GetCurrentDarkTheme(settingsRepository = get())
    }
}
