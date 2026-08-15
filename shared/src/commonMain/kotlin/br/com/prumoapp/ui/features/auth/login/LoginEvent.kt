package br.com.prumoapp.ui.features.auth.login

import br.com.prumoapp.domain.model.auth.AuthCredentialData

sealed class LoginEvent {
    data object OnGoogleSignInClick : LoginEvent()
    data class OnGoogleCredentialReceived(val credential: AuthCredentialData) : LoginEvent()
    data class OnGoogleSignInError(val errorMessage: String) : LoginEvent()
    data object OnGoogleSignInCancelled : LoginEvent()
    data object OnGoogleSignInCompleted : LoginEvent()
}