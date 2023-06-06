package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.SharingRepository

class OpenTerms(private val sharingRepository: SharingRepository) {

    operator fun invoke(link: String) {
        sharingRepository.openTerms(link)
    }
}