package br.com.prumoapp.data.mapper

import br.com.prumoapp.domain.model.auth.AuthCredentialData
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.GoogleAuthProvider

fun AuthCredentialData.toFirebaseCredential(): AuthCredential {
    return when (this) {
        is AuthCredentialData.Google -> {
            GoogleAuthProvider.credential(idToken, accessToken)
        }
    }
}