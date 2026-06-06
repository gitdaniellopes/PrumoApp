package br.com.prumoapp.domain.extensions.auth

sealed class AuthException : Exception() {
    data class UserDisabledException(override val message: String) : AuthException()
    data class TooManyRequestsException(override val message: String) : AuthException()
    data class UnknownException(override val message: String) : AuthException()
}