package com.example.playlistmaker.settings.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.usecase.ChangeAppTheme
import com.example.playlistmaker.settings.domain.usecase.GetCurrentDarkTheme
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendMailToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp
import com.example.playlistmaker.sharing.domain.model.EmailData

class SettingsViewModel(
    getCurrentDarkThemeUseCase: GetCurrentDarkTheme,
    private val changeAppThemeUseCase: ChangeAppTheme,
    private val openTermsUseCase: OpenTerms,
    private val sendMailToSupportUseCase: SendMailToSupport,
    private val shareAppUseCase: ShareApp
) : ViewModel() {
    private val _appThemeIsDark = MutableLiveData<Boolean>()
    val appThemeIsDark: LiveData<Boolean> = _appThemeIsDark

    init {
        _appThemeIsDark.value = getCurrentDarkThemeUseCase()
    }

    fun switchTheme(togglePosition: Boolean) {
        changeAppThemeUseCase(togglePosition)
        _appThemeIsDark.value = togglePosition
    }

    fun shareApp(link: String) {
        shareAppUseCase(link)
    }

    fun openTerms(termsLink: String) {
        openTermsUseCase(termsLink)
    }

    fun sendMailToSupport(emailData: EmailData) {
        sendMailToSupportUseCase(emailData)
    }
}