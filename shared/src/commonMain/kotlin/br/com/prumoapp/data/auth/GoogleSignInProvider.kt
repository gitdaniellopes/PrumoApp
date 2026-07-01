package br.com.prumoapp.data.auth

import androidx.compose.runtime.Composable
import br.com.prumoapp.domain.model.user.User

interface GoogleSignInProvider {
    suspend fun signInWithGoogle(): GoogleSignInResult?
}

@Composable
expect fun rememberGoogleAuthUiProvider(): GoogleSignInProvider

sealed class GoogleSignInResult {
    data class Credential(val idToken: String, val accessToken: String) : GoogleSignInResult()
    data class SignedInUser(val user: User) : GoogleSignInResult()
    data class Error(val message: String) : GoogleSignInResult()
}