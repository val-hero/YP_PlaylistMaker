package com.example.playlistmaker.settings.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.data.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.GetCurrentDarkTheme
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendMailToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp

class SettingsViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val settingsRepository by lazy(LazyThreadSafetyMode.NONE) { SettingsRepositoryImpl(context) }
    private val sharingRepository by lazy(LazyThreadSafetyMode.NONE) { SharingRepositoryImpl(ExternalNavigator(context))}

    private val changeAppThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ChangeAppTheme(settingsRepository)
    }

    private val getCurrentDarkThemeUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetCurrentDarkTheme(settingsRepository)
    }

    private val openTermsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        OpenTerms(sharingRepository)
    }

    private val sendMailToSupportUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SendMailToSupport(sharingRepository)
    }

    private val shareAppUseCase by lazy(LazyThreadSafetyMode.NONE) {
        ShareApp(sharingRepository)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            getCurrentDarkThemeUseCase = getCurrentDarkThemeUseCase,
            changeAppThemeUseCase = changeAppThemeUseCase,
            openTermsUseCase = openTermsUseCase,
            sendMailToSupportUseCase = sendMailToSupportUseCase,
            shareAppUseCase = shareAppUseCase
        ) as T
    }
}