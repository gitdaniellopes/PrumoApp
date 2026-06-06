package br.com.prumoapp.di

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.dsl.module

val dataSourceModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }
}