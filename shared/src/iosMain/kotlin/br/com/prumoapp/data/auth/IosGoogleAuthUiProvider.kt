package br.com.prumoapp.data.auth

import br.com.prumoapp.core.logging.AppLogger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private const val TAG = "IosGoogleAuthUiProvider"

class IosGoogleAuthUiProvider : GoogleSignInProvider {

    override suspend fun signInWithGoogle(): GoogleSignInResult? {
        val helper = GoogleSignInRegistry.helper
        if (helper == null) {
            AppLogger.e(TAG, "GoogleSignInHelper é nulo")
            return null
        }

        return suspendCancellableCoroutine { continuation ->
            helper.signIn(object : GoogleSignInCallback {
                override fun onSuccess(idToken: String, accessToken: String) {
                    continuation.resume(GoogleSignInResult.Credential(idToken, accessToken))
                }

                override fun onCancel() {
                    continuation.resume(null)
                }

                override fun onFailure(errorMessage: String) {
                    continuation.resume(GoogleSignInResult.Error(errorMessage))
                }
            })
        }
    }
}