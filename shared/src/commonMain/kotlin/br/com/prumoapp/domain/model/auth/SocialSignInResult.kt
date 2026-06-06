package br.com.prumoapp.domain.model.auth

import br.com.prumoapp.domain.model.user.UserId

data class SocialSignInResult(
    val uid: UserId,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val isNewUser: Boolean
)
