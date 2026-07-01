package br.com.prumoapp.data.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager

@Composable
actual fun rememberGoogleAuthUiProvider(): GoogleSignInProvider {
    val context = LocalContext.current
    return remember(context) {
        AndroidGoogleAuthUiProvider(
            context = context,
            credentialManager = CredentialManager.create(context)
        )
    }
}