package br.com.prumoapp.ui.features.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import br.com.prumoapp.data.auth.GoogleSignInResult
import br.com.prumoapp.data.auth.rememberGoogleAuthUiProvider
import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.ui.features.auth.login.componets.LoginContent
import kotlinx.coroutines.flow.Flow

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    sideLoginEffect: Flow<LoginEffect>,
    onEvent: (LoginEvent) -> Unit
) {
    val googleAuthProvider = rememberGoogleAuthUiProvider()

    LaunchedEffect(sideLoginEffect) {
        sideLoginEffect.collect { effect ->
            when (effect) {
                is LoginEffect.LaunchGoogleSignIn -> {
                    when (val result = googleAuthProvider.signInWithGoogle()) {
                        is GoogleSignInResult.Credential -> {
                            onEvent(
                                LoginEvent.OnGoogleCredentialReceived(
                                    AuthCredentialData.Google(
                                        idToken = result.idToken,
                                        accessToken = result.accessToken
                                    )
                                )
                            )
                        }

                        is GoogleSignInResult.Error -> {
                            onEvent(LoginEvent.OnGoogleSignInError(result.message))
                        }

                        is GoogleSignInResult.SignedInUser -> {
                            onEvent(LoginEvent.OnGoogleSignInCompleted)
                        }

                        null -> {
                            onEvent(LoginEvent.OnGoogleSignInCancelled)
                        }
                    }
                }

                is LoginEffect.ShowToast -> {

                }
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onGoogleSignIn = { onEvent(LoginEvent.OnGoogleSignInClick) }
    )
}