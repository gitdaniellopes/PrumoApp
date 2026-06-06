package br.com.prumoapp.domain.model.auth

import br.com.prumoapp.domain.model.user.UserId

data class AuthUser(
    val uid: UserId,
    val email: String?
)
