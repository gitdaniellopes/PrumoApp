package br.com.prumoapp.di

import br.com.prumoapp.data.repository.AuthRepositoryImpl
import br.com.prumoapp.data.repository.FixedExpenseRepositoryImpl
import br.com.prumoapp.data.repository.InstallmentSeriesRepositoryImpl
import br.com.prumoapp.data.repository.StandardExpenseRepositoryImpl
import br.com.prumoapp.data.repository.UserRepositoryImpl
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.FixedExpenseRepository
import br.com.prumoapp.domain.repository.InstallmentSeriesRepository
import br.com.prumoapp.domain.repository.StandardExpenseRepository
import br.com.prumoapp.domain.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class

    singleOf(::StandardExpenseRepositoryImpl) bind StandardExpenseRepository::class
    singleOf(::InstallmentSeriesRepositoryImpl) bind InstallmentSeriesRepository::class
    singleOf(::FixedExpenseRepositoryImpl) bind FixedExpenseRepository::class
}