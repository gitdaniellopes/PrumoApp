package br.com.prumoapp.di

import br.com.prumoapp.data.repository.AuthRepositoryImpl
import br.com.prumoapp.data.repository.UserRepositoryImpl
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
}