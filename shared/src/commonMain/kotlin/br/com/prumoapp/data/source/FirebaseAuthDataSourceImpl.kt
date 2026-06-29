package br.com.prumoapp.data.source

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.data.mapper.toAuthUser
import br.com.prumoapp.data.mapper.toFirebaseCredential
import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.model.auth.AuthUser
import br.com.prumoapp.domain.model.auth.SocialSignInResult
import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.source.AuthRemoteDataSource
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FirebaseAuthDataSourceImpl(
    private val auth: FirebaseAuth
) : AuthRemoteDataSource {

    override val currentUser: AuthUser?
        get() = auth.currentUser?.toAuthUser()

    override fun observeAuthState(): Flow<AuthUser?> {
        return auth.authStateChanged.map { firebaseUser ->
            firebaseUser?.toAuthUser()
        }
    }

    override suspend fun loginWithGoogle(credential: AuthCredentialData): Result<SocialSignInResult> {
        return try {
            AppLogger.d("FirebaseAuthDataSource", "Criando credential do Firebase...")
            val firebaseCredential = credential.toFirebaseCredential()
            AppLogger.d("FirebaseAuthDataSource", "Chamando signInWithCredential...")

            val authResult = auth.signInWithCredential(firebaseCredential)
            val user = authResult.user
                ?: return Result.failure(IllegalStateException("Autenticação bem-sucedida, mas usuario pe nulo"))

            AppLogger.d("FirebaseAuthDataSource", "Firebase Ok - uid=${user.uid}")

            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

            Result.success(
                SocialSignInResult(
                    uid = UserId(user.uid),
                    email = user.email,
                    displayName = user.displayName,
                    photoUrl = user.photoURL,
                    isNewUser = isNewUser
                )
            )
        } catch (e: Exception) {
            AppLogger.e("FirebaseAuthDataSource", "Error: ${e.message} - ${e.cause}", e)
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        return auth.signOut()
    }

    override suspend fun deleteCurrentUser(): Result<Unit> {
        return try {
            auth.currentUser?.delete()
            Result.success(Unit)
        } catch (e: Exception) {
            AppLogger.e("FirebaseAuthDataSource", "Erro ao deletar usuario", e)
            Result.failure(e)
        }
    }
}