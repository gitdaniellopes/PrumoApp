package br.com.prumoapp.data.repository

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.domain.extensions.auth.AuthException
import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.model.auth.AuthUser
import br.com.prumoapp.domain.model.auth.SocialSignInResult
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.source.AuthRemoteDataSource
import br.com.prumoapp.domain.source.UserRemoteDataSource
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val authDataSource: AuthRemoteDataSource,
    private val userDataSource: UserRemoteDataSource,
) : AuthRepository {

    override val currentUser: AuthUser?
        get() = authDataSource.currentUser

    override fun observeAuthState(): Flow<AuthUser?> {
        return authDataSource.observeAuthState()
    }

    override suspend fun loginWithGoogle(credential: AuthCredentialData): Result<SocialSignInResult> {
        return try {
            val result = authDataSource.loginWithGoogle(credential).getOrThrow()
            Result.success(result)
        } catch (e: Exception) {
            AppLogger.e(
                "AuthRepository",
                "Erro ao fazer login com Google - ${e.message} - ${e.cause}",
                e
            )
            Result.failure(mapFirebaseException(e))
        }
    }

    override suspend fun getFirestoreUser(): User? {
        val authUser = authDataSource.currentUser ?: return null
        return userDataSource.getUser(authUser.uid).getOrNull()
    }

    override suspend fun signOut() {
        authDataSource.signOut()
    }

    override suspend fun deleteCurrentUser(): Result<Unit> {
        return try {
            authDataSource.deleteCurrentUser().getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            AppLogger.e(
                "AuthRepository",
                "Erro ao deletar usuario - ${e.message} - ${e.cause}",
                e
            )
            Result.failure(mapFirebaseException(e))
        }
    }

    private fun mapFirebaseException(e: Exception): AuthException {
        return AuthException.UnknownException(e.message.toString())
    }
}