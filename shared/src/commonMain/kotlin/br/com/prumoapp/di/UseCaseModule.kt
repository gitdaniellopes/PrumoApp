package br.com.prumoapp.di

import br.com.prumoapp.domain.usecases.LoginWithGoogleUseCase
import br.com.prumoapp.domain.usecases.LoginWithGoogleUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::LoginWithGoogleUseCaseImpl) bind LoginWithGoogleUseCase::class
}