package br.com.prumoapp.di

import br.com.prumoapp.data.source.FirebaseAuthDataSourceImpl
import br.com.prumoapp.data.source.FirestoreUserDataSourceImpl
import br.com.prumoapp.domain.source.AuthRemoteDataSource
import br.com.prumoapp.domain.source.UserRemoteDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {
    single { Firebase.auth }
    single { Firebase.firestore }

    singleOf(::FirestoreUserDataSourceImpl) bind UserRemoteDataSource::class
    singleOf(::FirebaseAuthDataSourceImpl) bind AuthRemoteDataSource::class
}