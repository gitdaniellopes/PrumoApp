package br.com.prumoapp.data.auth

interface GoogleSignInCallback {
    fun onSuccess(idToken: String, accessToken: String)
    fun onCancel()
    fun onFailure(errorMessage: String)
}

interface GoogleSignInHelper {
    fun signIn(callback: GoogleSignInCallback)
}

internal object GoogleSignInRegistry {
    var helper: GoogleSignInHelper? = null
}