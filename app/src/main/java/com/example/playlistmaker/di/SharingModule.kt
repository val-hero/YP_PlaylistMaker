package com.example.playlistmaker.di

import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.SharingRepository
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendMailToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp
import org.koin.dsl.module

val sharingModule = module {

    single<SharingRepository> {
        SharingRepositoryImpl(externalNavigator = get())
    }

    single {
        ExternalNavigator(context = get())
    }

    factory {
        OpenTerms(sharingRepository = get())
    }

    factory {
        SendMailToSupport(sharingRepository = get())
    }

    factory {
        ShareApp(sharingRepository = get())
    }
}