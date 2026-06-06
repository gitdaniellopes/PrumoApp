package br.com.prumoapp.domain.extensions

import br.com.prumoapp.domain.model.user.UserId
import br.com.prumoapp.domain.repository.AuthRepository

fun AuthRepository.requireUserId(): Result<UserId> =
    currentUser?.uid?.let { Result.success(it) }
        ?: Result.failure(IllegalStateException("Usuario não autenticado."))