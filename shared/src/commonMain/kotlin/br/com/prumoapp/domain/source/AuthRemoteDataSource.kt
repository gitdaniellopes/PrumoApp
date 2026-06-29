package br.com.prumoapp.domain.source

import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.model.auth.AuthUser
import br.com.prumoapp.domain.model.auth.SocialSignInResult
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {

    val currentUser: AuthUser?
    fun observeAuthState(): Flow<AuthUser?>
    suspend fun loginWithGoogle(credential: AuthCredentialData): Result<SocialSignInResult>
    suspend fun signOut()
    suspend fun deleteCurrentUser(): Result<Unit>
}