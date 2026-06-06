package br.com.prumoapp.domain.model.user

import br.com.prumoapp.domain.model.auth.SocialSignInResult
import br.com.prumoapp.domain.model.common.Money
import kotlin.time.Instant

data class User(
    val id: UserId,
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val createdAt: Instant? = null,
    val monthlyBudget: Money? = null
) {
    companion object {
        fun fromSocialSignIn(result: SocialSignInResult): User {
            return User(
                id = result.uid,
                name = result.displayName ?: "",
                email = result.email ?: "",
                photoUrl = result.photoUrl
            )
        }
    }
}
