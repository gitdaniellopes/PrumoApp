package br.com.prumoapp.domain.model.auth

sealed class AuthCredentialData {
    data class Google(val idToken: String, val accessToken: String? = null) : AuthCredentialData()
}