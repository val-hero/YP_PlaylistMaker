package com.example.playlistmaker.sharing.domain.usecase

import com.example.playlistmaker.sharing.domain.SharingRepository

class ShareApp(private val sharingRepository: SharingRepository) {

    operator fun invoke(link: String) {
        sharingRepository.shareApp(link)
    }
}