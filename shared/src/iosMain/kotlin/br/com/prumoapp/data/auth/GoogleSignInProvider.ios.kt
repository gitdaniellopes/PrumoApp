package br.com.prumoapp.data.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberGoogleAuthUiProvider(): GoogleSignInProvider {
    return remember { IosGoogleAuthUiProvider() }
}