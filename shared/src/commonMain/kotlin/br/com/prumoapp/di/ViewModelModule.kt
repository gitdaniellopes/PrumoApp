package br.com.prumoapp.di

import br.com.prumoapp.MainViewModel
import br.com.prumoapp.ui.features.auth.login.LoginViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::LoginViewModel)
}