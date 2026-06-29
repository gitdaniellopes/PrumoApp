package br.com.prumoapp.domain.repository

import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.model.auth.AuthUser
import br.com.prumoapp.domain.model.auth.SocialSignInResult
import br.com.prumoapp.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: AuthUser?
    fun observeAuthState(): Flow<AuthUser?>
    suspend fun loginWithGoogle(credential: AuthCredentialData): Result<SocialSignInResult>
    suspend fun getFirestoreUser(): User?
    suspend fun signOut()
    suspend fun deleteCurrentUser(): Result<Unit>
}