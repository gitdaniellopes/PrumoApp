package br.com.prumoapp.ui.features.auth.login

sealed interface LoginEffect {
    data class ShowToast(val message: String) : LoginEffect
    data object LaunchGoogleSignIn : LoginEffect
}