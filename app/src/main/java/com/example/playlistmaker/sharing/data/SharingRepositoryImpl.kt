package com.example.playlistmaker.sharing.data

import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class SharingRepositoryImpl(private val externalNavigator: ExternalNavigator) : SharingRepository {

    override fun shareApp(link: String) {
        externalNavigator.shareLink(link)
    }

    override fun sendToSupport(emailData: EmailData) {
        externalNavigator.openEmail(emailData)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(link)
    }


}