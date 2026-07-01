package br.com.prumoapp.domain.usecases

import br.com.prumoapp.core.logging.AppLogger
import br.com.prumoapp.domain.model.auth.AuthCredentialData
import br.com.prumoapp.domain.model.user.User
import br.com.prumoapp.domain.repository.AuthRepository
import br.com.prumoapp.domain.repository.UserRepository

interface LoginWithGoogleUseCase {
    suspend operator fun invoke(credentialData: AuthCredentialData): Result<Unit>
}

class LoginWithGoogleUseCaseImpl(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : LoginWithGoogleUseCase {

    override suspend fun invoke(credentialData: AuthCredentialData): Result<Unit> = runCatching {
        val result = authRepository.loginWithGoogle(credentialData).getOrThrow()

        if (result.isNewUser) {
            userRepository.createUser(User.fromSocialSignIn(result)).onFailure { error ->
                AppLogger.e(
                    "LoginWithGoogleUseCase",
                    "Falha ao criar usuario no primeiro acesso",
                    error
                )
            }.getOrThrow()
        }
    }
}