package com.example.playlistmaker.sharing.domain

import com.example.playlistmaker.sharing.domain.model.EmailData

interface SharingRepository {

    fun shareApp(link: String)

    fun sendToSupport(emailData: EmailData)

    fun openTerms(link: String)

}