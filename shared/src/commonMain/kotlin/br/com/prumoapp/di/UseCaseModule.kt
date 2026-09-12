package br.com.prumoapp.di

import br.com.prumoapp.domain.usecases.LoginWithGoogleUseCase
import br.com.prumoapp.domain.usecases.LoginWithGoogleUseCaseImpl
import br.com.prumoapp.domain.usecases.expense.AddExpenseUseCase
import br.com.prumoapp.domain.usecases.expense.AddExpenseUseCaseImpl
import br.com.prumoapp.domain.usecases.expense.MaterializeFixedExpenseUseCase
import br.com.prumoapp.domain.usecases.expense.MaterializeFixedExpenseUseCaseImpl
import br.com.prumoapp.domain.usecases.expense.TogglePaidUseCase
import br.com.prumoapp.domain.usecases.expense.TogglePaidUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::LoginWithGoogleUseCaseImpl) bind LoginWithGoogleUseCase::class

    factoryOf(::AddExpenseUseCaseImpl) bind AddExpenseUseCase::class
    factoryOf(::MaterializeFixedExpenseUseCaseImpl) bind MaterializeFixedExpenseUseCase::class
    factoryOf(::TogglePaidUseCaseImpl) bind TogglePaidUseCase::class
}