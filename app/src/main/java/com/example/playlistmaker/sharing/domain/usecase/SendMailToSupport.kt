package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.model.EmailData

class SendMailToSupport(private val sharingRepository: SharingRepository) {

    operator fun invoke(emailData: EmailData) {
        sharingRepository.sendToSupport(emailData)
    }
}